package com.uade.tpo.ecommerce.service;

import com.uade.tpo.ecommerce.model.Favorite;
import java.util.List;

public interface IFavoriteService {
    List<Favorite> getFavoritesByUser(Long userId);
    Favorite addFavorite(Long userId, Long productId);
    void removeFavorite(Long userId, Long productId);
    boolean isFavorite(Long userId, Long productId);
}
