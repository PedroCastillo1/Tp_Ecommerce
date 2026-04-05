package com.uade.tpo.ecommerce.controller;

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

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @PostMapping("/{userId}/agregar/{productId}")
    public CartItem addItem(@PathVariable Long userId, @PathVariable Long productId, @RequestParam Integer cantidad) {
        return cartService.addItem(userId, productId, cantidad);
    }

    @PostMapping("/{userId}/checkout")
    public BigDecimal checkout(@PathVariable Long userId) {
        return cartService.checkout(userId);
    }

    @DeleteMapping("/{userId}/vaciar")
    public void clear(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}