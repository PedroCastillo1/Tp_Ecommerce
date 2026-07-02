package com.uade.tpo.ecommerce.service;

// ## INTERFAZ — Contrato del Servicio de Carrito
// ##
// ## Define las operaciones que debe implementar CartService.
// ## Usar interfaces permite cambiar la implementación sin modificar los controllers.
// ##
// ## checkout() es el método principal del TPO:
// ##   verifica stock → descuenta stock → guarda historial → vacía carrito → retorna total

import java.math.BigDecimal;
import java.util.List;

import com.uade.tpo.ecommerce.model.CartItem;

public interface ICartService {
    CartItem addItem(Long userId, Long productId, Integer quantity); // ## agregar producto al carrito
    void removeItem(Long itemId);                                    // ## eliminar item por ID
    void clearCart(Long userId);                                     // ## vaciar todo el carrito
    List<CartItem> getCartByUser(Long userId);                       // ## obtener items del carrito
    BigDecimal checkout(Long userId);                                // ## confirmar compra (método principal)
}
