package com.uade.tpo.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.ecommerce.model.Order;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByFechaDesc(Long userId);
}
