package com.uade.tpo.ecommerce.service;

// ## SERVICIO — Carrito de Compras
// ##
// ## Lógica de negocio para gestionar el carrito y procesar el checkout.
// ## Es llamado por CartController.
// ##
// ## checkout() es el método más importante:
// ##   1. Obtiene todos los CartItems del usuario
// ##   2. Crea la Order (guarda primero para tener el ID)
// ##   3. Por cada item: verifica stock → descuenta stock → crea OrderItem
// ##   4. Actualiza la Order con items y total → guarda en DB
// ##   5. Vacía el carrito
// ##   6. Retorna el total de la compra

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.ecommerce.exception.ProductoNotFoundException;
import com.uade.tpo.ecommerce.model.CartItem;
import com.uade.tpo.ecommerce.model.Order;
import com.uade.tpo.ecommerce.model.OrderItem;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.repository.ICartRepository;
import com.uade.tpo.ecommerce.repository.IOrderRepository;
import com.uade.tpo.ecommerce.repository.IProductosRepository;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductosRepository productosRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderRepository orderRepository;

    // ## Agrega un producto al carrito del usuario.
    // ## Verifica stock antes de guardar (no permite agregar sin stock suficiente).
    @Override
    public CartItem addItem(Long userId, Long productId, Integer quantity) {
        Product product = productosRepository.findById(productId)
                .orElseThrow(() -> new ProductoNotFoundException(productId));
        User user = userService.findUserById(userId);

        if (product.getStock() < quantity) {
            throw new RuntimeException("No hay stock suficiente");
        }

        CartItem item = new CartItem(product, user, quantity);
        return cartRepository.save(item);
    }

    // ## Obtiene todos los items del carrito de un usuario desde la DB
    @Override
    public List<CartItem> getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // ## Elimina un item específico del carrito por su ID
    @Override
    public void removeItem(Long itemId) {
        cartRepository.deleteById(itemId);
    }

    // ## Elimina todos los items del carrito de un usuario
    @Override
    public void clearCart(Long userId) {
        List<CartItem> items = getCartByUser(userId);
        cartRepository.deleteAll(items);
    }

    // ## Procesa el checkout completo:
    // ##   - Verifica stock de todos los items
    // ##   - Descuenta el stock de cada producto en la DB
    // ##   - Crea una Order con OrderItems (queda en historial permanente)
    // ##   - Vacía el carrito del usuario
    // ##   - Retorna el total de la compra
    @Override
    public BigDecimal checkout(Long userId) {
        List<CartItem> items = getCartByUser(userId);
        User user = userService.findUserById(userId);
        BigDecimal total = BigDecimal.ZERO;

        // ## Creamos la Order primero y la guardamos para obtener su ID generado
        // ## (necesario para crear los OrderItems que referencian la Order)
        Order order = new Order();
        order.setUser(user);
        order.setFecha(java.time.LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : items) {
            Product p = item.getProduct();

            // ## Doble verificación de stock por si cambió desde que se agregó al carrito
            if (p.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock agotado para: " + p.getName());
            }

            // ## Descontar el stock del producto en la DB
            p.setStock(p.getStock() - item.getQuantity());
            productosRepository.save(p);

            // ## Calcular subtotal y acumular al total de la orden
            BigDecimal subtotal = p.getPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(subtotal);

            // ## Crear el OrderItem con snapshot del precio actual
            // ## (el precio puede cambiar después — guardamos el precio de este momento)
            OrderItem oi = new OrderItem(savedOrder, p, item.getQuantity(), p.getPrice());
            orderItems.add(oi);
        }

        // ## Actualizar la orden con los items y el total calculado, y guardar definitivamente
        savedOrder.setItems(orderItems);
        savedOrder.setTotal(total);
        orderRepository.save(savedOrder);

        // ## Vaciar el carrito después de la compra exitosa
        clearCart(userId);
        return total;
    }
}
