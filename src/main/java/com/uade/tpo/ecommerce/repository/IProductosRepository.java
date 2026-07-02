package com.uade.tpo.ecommerce.repository;

// ## REPOSITORIO — Productos
// ##
// ## Acceso a la tabla "product" en la DB via Spring Data JPA.
// ## Extiende JpaRepository que provee:
// ##   findAll()       → lista todos los productos
// ##   findById(id)    → busca por ID (devuelve Optional)
// ##   save(product)   → guarda o actualiza un producto
// ##   deleteById(id)  → elimina por ID
// ##   existsById(id)  → verifica existencia
// ##   count()         → cuenta total de registros (usado en DataInitializer)
// ##
// ## No tiene métodos personalizados — el filtrado y ordenamiento
// ## se hace en ProductService con Java Streams.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.Product;

@Repository
public interface IProductosRepository extends JpaRepository<Product, Long> {
    // ## Los métodos heredados de JpaRepository son suficientes para este caso
}
