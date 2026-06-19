package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Historial de Compras
// ##
// ## Expone el endpoint para consultar las órdenes pasadas de un usuario.
// ## Las órdenes se crean automáticamente en CartService.checkout().
// ## Este controller solo las lee — no las crea ni las modifica.
// ##
// ## Endpoint:
// ##   GET /api/ordenes/{userId}
// ##     → Lista de Order[] del usuario, ordenadas de más reciente a más antigua
// ##     → Cada Order incluye items con producto, cantidad y precioUnitario

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.ecommerce.model.Order;
import com.uade.tpo.ecommerce.repository.IOrderRepository;

@RestController
@RequestMapping("/api/ordenes")
public class OrderController {

    @Autowired
    private IOrderRepository orderRepository;

    // ## GET /api/ordenes/{userId}
    // ## Usa el método del repositorio que aplica ORDER BY fecha DESC
    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderRepository.findByUserIdOrderByFechaDesc(userId);
    }
}
