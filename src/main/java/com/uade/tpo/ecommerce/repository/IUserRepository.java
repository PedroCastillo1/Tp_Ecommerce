package com.uade.tpo.ecommerce.repository;

// ## REPOSITORIO — Usuarios
// ##
// ## Acceso a la tabla "usuarios" en la DB via Spring Data JPA.
// ##
// ## Métodos personalizados:
// ##   findByEmail      → busca usuario por email (usado en autenticación y /me)
// ##   existsByEmail    → verifica unicidad de email en registro (query JPQL explícita)
// ##   existsByUsername → verifica unicidad de username en registro (query JPQL explícita)
// ##
// ## Se usan queries JPQL (@Query) para existsByEmail y existsByUsername porque
// ## Spring Data no genera automáticamente métodos "existsBy" para campos que no
// ## son el ID en todas las versiones. Las queries son más explícitas y seguras.

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // ## Busca un usuario por email — usado en login, /me y carga de UserDetails
    Optional<User> findByEmail(String email);

    // ## Verifica si ya existe un usuario con ese email (para validar unicidad en registro)
    // ## COUNT(u) > 0 devuelve boolean directamente sin necesidad de Optional
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // ## Verifica si ya existe un usuario con ese username (para validar unicidad en registro)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);
}
