package com.uade.tpo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import com.uade.tpo.ecommerce.model.CartItem;

public interface ICartService {
    CartItem addItem(Long userId, Long productId, Integer quantity);
    void removeItem(Long itemId);
    void clearCart(Long userId);
    List<CartItem> getCartByUser(Long userId);
    BigDecimal checkout(Long userId); // El método estrella del TPO
}