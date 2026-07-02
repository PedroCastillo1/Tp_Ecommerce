package com.uade.tpo.ecommerce.repository;

// ## REPOSITORIO — Categorías
// ##
// ## Acceso a la tabla "Categoria" en la DB via Spring Data JPA.
// ## Extiende JpaRepository que provee CRUD completo automáticamente.
// ##
// ## Método personalizado:
// ##   findByName → genera automáticamente:
// ##   SELECT * FROM Categoria WHERE name = ?
// ##   Útil para verificar duplicados por nombre antes de crear una categoría

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.Category;

@Repository
public interface ICategoriaRepository extends JpaRepository<Category, Long> {

    // ## Busca una categoría por su nombre exacto (case-sensitive)
    Category findByName(String name);
}
