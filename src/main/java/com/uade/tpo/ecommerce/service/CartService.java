package com.uade.tpo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.ecommerce.model.CartItem;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.repository.ICartRepository; // Debés crear esta interfaz igual a las otras

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Override
    public CartItem addItem(Long userId, Long productId, Integer quantity) {
        Product product = productService.findProductById(productId);
        User user = userService.findUserById(userId);

        // Validación de stock que pide la consigna
        if (product.getStock() < quantity) {
            throw new RuntimeException("No hay stock suficiente");
        }

        CartItem item = new CartItem(product, user, quantity);
        return cartRepository.save(item);
    }

    @Override
    public List<CartItem> getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId); // Necesitás este método en el Repo
    }

    @Override
    public void removeItem(Long itemId) {
        cartRepository.deleteById(itemId);
    }

    @Override
    public void clearCart(Long userId) {
        List<CartItem> items = getCartByUser(userId);
        cartRepository.deleteAll(items);
    }

    @Override
    public BigDecimal checkout(Long userId) {
        List<CartItem> items = getCartByUser(userId);
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            Product p = item.getProduct();
            
            // Validamos stock final antes de procesar
            if (p.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock agotado para: " + p.getName());
            }

            // Descontamos stock como pide el TPO
            p.setStock(p.getStock() - item.getQuantity());
            productService.createProduct(p); // Actualizamos el producto en la BD

            // Calculamos el total
            BigDecimal subtotal = p.getPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(subtotal);
        }

        // Vaciamos el carrito después del checkout
        clearCart(userId);
        return total;
    }
}