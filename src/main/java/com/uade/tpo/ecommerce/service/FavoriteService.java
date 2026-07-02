package com.uade.tpo.ecommerce.service;

// ## SERVICIO — Favoritos
// ##
// ## Lógica de negocio para gestionar la lista de favoritos de un usuario.
// ## Es llamado por FavoriteController.
// ##
// ## Patrón "upsert" en addFavorite:
// ##   Si el favorito ya existe → lo devuelve sin crear un duplicado
// ##   Si no existe            → lo crea y lo guarda en DB
// ##
// ## @Transactional en removeFavorite:
// ##   Necesario porque deleteByUserIdAndProductId es una query de eliminación personalizada
// ##   (Spring Data JPA requiere transacción para queries de escritura en métodos de repositorio)

import com.uade.tpo.ecommerce.exception.ProductoNotFoundException;
import com.uade.tpo.ecommerce.model.Favorite;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.repository.IFavoriteRepository;
import com.uade.tpo.ecommerce.repository.IProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService implements IFavoriteService {

    @Autowired
    private IFavoriteRepository favoriteRepository;

    @Autowired
    private IProductosRepository productosRepository;

    @Autowired
    private IUserService userService;

    // ## Obtiene todos los favoritos de un usuario desde la DB
    @Override
    public List<Favorite> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    // ## Agrega un producto a favoritos:
    // ##   1. Busca si ya existe el favorito para este user + product
    // ##   2. Si existe → lo devuelve (sin duplicar)
    // ##   3. Si no existe → verifica que el producto exista y crea el favorito
    @Override
    public Favorite addFavorite(Long userId, Long productId) {
        return favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> {
                    Product product = productosRepository.findById(productId)
                            .orElseThrow(() -> new ProductoNotFoundException(productId));
                    User user = userService.findUserById(userId);
                    return favoriteRepository.save(new Favorite(product, user));
                });
    }

    // ## Elimina el favorito de la DB usando una query por userId + productId
    // ## @Transactional requerido por Spring Data para queries de eliminación custom
    @Override
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    // ## Verifica si un producto ya es favorito de un usuario (true/false)
    @Override
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
