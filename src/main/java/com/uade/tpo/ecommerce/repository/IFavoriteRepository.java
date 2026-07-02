package com.uade.tpo.ecommerce.repository;

// ## REPOSITORIO — Favoritos
// ##
// ## Acceso a la tabla "favorite" en la DB via Spring Data JPA.
// ##
// ## Métodos personalizados:
// ##   findByUserId               → lista los favoritos de un usuario
// ##   findByUserIdAndProductId   → busca un favorito específico (para evitar duplicados)
// ##   deleteByUserIdAndProductId → elimina un favorito por usuario + producto
// ##
// ## Spring Data genera automáticamente el SQL de todos estos métodos
// ## basándose en el nombre del método (convención "findBy", "deleteBy", etc.)

import com.uade.tpo.ecommerce.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

    // ## Lista todos los favoritos de un usuario
    List<Favorite> findByUserId(Long userId);

    // ## Busca un favorito específico por usuario + producto (para verificar duplicados)
    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);

    // ## Elimina el favorito de un usuario+producto específico
    // ## Requiere @Transactional en el servicio que lo llama
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
