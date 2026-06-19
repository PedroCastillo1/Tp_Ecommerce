package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Carrito de Compras
// ##
// ## Expone los endpoints REST para gestionar el carrito de un usuario.
// ## Todas las rutas requieren autenticación (configurado en SecurityConfig).
// ##
// ## Endpoints:
// ##   GET    /api/carrito/{userId}                        → obtener items del carrito
// ##   POST   /api/carrito/{userId}/agregar/{productId}    → agregar producto con cantidad
// ##   POST   /api/carrito/{userId}/checkout               → confirmar compra
// ##   DELETE /api/carrito/{userId}/vaciar                 → vaciar el carrito

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.ecommerce.model.CartItem;
import com.uade.tpo.ecommerce.service.ICartService;

@RestController
@RequestMapping("/api/carrito")
public class CartController {

    @Autowired
    private ICartService cartService;

    // ## GET /api/carrito/{userId}
    // ## Devuelve todos los CartItems del usuario (con producto y cantidad)
    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    // ## POST /api/carrito/{userId}/agregar/{productId}?cantidad=N
    // ## Agrega N unidades de un producto al carrito del usuario
    @PostMapping("/{userId}/agregar/{productId}")
    public CartItem addItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer cantidad) {
        return cartService.addItem(userId, productId, cantidad);
    }

    // ## POST /api/carrito/{userId}/checkout
    // ## Procesa la compra: verifica stock, descuenta, guarda historial, vacía carrito
    // ## Devuelve el total de la compra (BigDecimal)
    @PostMapping("/{userId}/checkout")
    public BigDecimal checkout(@PathVariable Long userId) {
        return cartService.checkout(userId);
    }

    // ## DELETE /api/carrito/{userId}/vaciar
    // ## Elimina todos los items del carrito sin procesar compra
    @DeleteMapping("/{userId}/vaciar")
    public void clear(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
