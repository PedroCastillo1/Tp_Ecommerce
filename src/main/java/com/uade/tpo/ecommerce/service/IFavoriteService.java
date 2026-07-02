package com.uade.tpo.ecommerce.service;

// ## INTERFAZ — Contrato del Servicio de Favoritos
// ##
// ## Define las operaciones que debe implementar FavoriteService.
// ## Patrón: userId + productId identifica unívocamente un favorito.

import com.uade.tpo.ecommerce.model.Favorite;
import java.util.List;

public interface IFavoriteService {
    List<Favorite> getFavoritesByUser(Long userId);                  // ## listar favoritos de un usuario
    Favorite addFavorite(Long userId, Long productId);               // ## agregar a favoritos (upsert)
    void removeFavorite(Long userId, Long productId);                // ## quitar de favoritos
    boolean isFavorite(Long userId, Long productId);                 // ## verificar si es favorito
}
