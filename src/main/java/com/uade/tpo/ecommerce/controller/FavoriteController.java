package com.uade.tpo.ecommerce.controller;

import com.uade.tpo.ecommerce.model.Favorite;
import com.uade.tpo.ecommerce.service.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;

    // GET /api/favoritos/{userId} — obtener favoritos del usuario
    @GetMapping("/{userId}")
    public List<Favorite> getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoritesByUser(userId);
    }

    // POST /api/favoritos/{userId}/agregar/{productId} — agregar a favoritos
    @PostMapping("/{userId}/agregar/{productId}")
    public Favorite addFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        return favoriteService.addFavorite(userId, productId);
    }

    // DELETE /api/favoritos/{userId}/eliminar/{productId} — quitar de favoritos
    @DeleteMapping("/{userId}/eliminar/{productId}")
    public void removeFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
    }
}
