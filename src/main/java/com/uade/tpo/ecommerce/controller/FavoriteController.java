package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Favoritos
// ##
// ## Expone los endpoints REST para gestionar los productos favoritos de un usuario.
// ## Todas las rutas requieren autenticación (configurado en SecurityConfig).
// ##
// ## Endpoints:
// ##   GET    /api/favoritos/{userId}                         → lista de favoritos del usuario
// ##   POST   /api/favoritos/{userId}/agregar/{productId}     → agregar producto a favoritos
// ##   DELETE /api/favoritos/{userId}/eliminar/{productId}    → quitar producto de favoritos

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

    // ## GET /api/favoritos/{userId}
    // ## Devuelve todos los Favorite del usuario (con el producto incluido)
    @GetMapping("/{userId}")
    public List<Favorite> getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoritesByUser(userId);
    }

    // ## POST /api/favoritos/{userId}/agregar/{productId}
    // ## Agrega un producto a favoritos. Si ya existe, devuelve el existente sin duplicar.
    @PostMapping("/{userId}/agregar/{productId}")
    public Favorite addFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        return favoriteService.addFavorite(userId, productId);
    }

    // ## DELETE /api/favoritos/{userId}/eliminar/{productId}
    // ## Elimina el favorito de la DB (por userId + productId)
    @DeleteMapping("/{userId}/eliminar/{productId}")
    public void removeFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
    }
}
