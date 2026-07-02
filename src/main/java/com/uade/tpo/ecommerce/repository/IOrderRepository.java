package com.uade.tpo.ecommerce.repository;

// ## REPOSITORIO — Historial de Compras
// ##
// ## Acceso a la tabla "ordenes" en la DB via Spring Data JPA.
// ##
// ## Método personalizado:
// ##   findByUserIdOrderByFechaDesc → Spring Data genera el SQL:
// ##   SELECT * FROM ordenes WHERE user_id = ? ORDER BY fecha DESC
// ##
// ## "OrderByFechaDesc" en el nombre del método es la convención de Spring Data
// ## para agregar ORDER BY fecha DESC a la query generada automáticamente.
// ## Esto asegura que el historial se muestra de más reciente a más antiguo.

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.ecommerce.model.Order;

public interface IOrderRepository extends JpaRepository<Order, Long> {

    // ## Devuelve las órdenes de un usuario ordenadas por fecha descendente (más reciente primero)
    List<Order> findByUserIdOrderByFechaDesc(Long userId);
}
