package com.uade.tpo.ecommerce.repository;

// ## REPOSITORIO — Carrito de Compras
// ##
// ## Acceso a la tabla "cart_item" en la DB via Spring Data JPA.
// ## Extiende JpaRepository que provee findAll(), findById(), save(), deleteById(), etc.
// ##
// ## Método personalizado:
// ##   findByUserId → Spring Data genera automáticamente la query SQL:
// ##   SELECT * FROM cart_item WHERE user_id = ?
// ##   El nombre del método sigue la convención "findBy" + campo de la entidad

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.CartItem;

@Repository
public interface ICartRepository extends JpaRepository<CartItem, Long> {

    // ## Devuelve todos los CartItems del usuario — usado en CartService.getCartByUser()
    List<CartItem> findByUserId(Long userId);
}
