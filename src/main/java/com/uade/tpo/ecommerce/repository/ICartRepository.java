package com.uade.tpo.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.CartItem;

@Repository
public interface ICartRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByUserId(Long userId);
}