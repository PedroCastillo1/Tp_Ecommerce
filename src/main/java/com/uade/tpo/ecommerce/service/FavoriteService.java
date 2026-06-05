package com.uade.tpo.ecommerce.service;

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

    @Override
    public List<Favorite> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public Favorite addFavorite(Long userId, Long productId) {
        // Si ya es favorito, lo devolvemos sin duplicar
        return favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> {
                    Product product = productosRepository.findById(productId)
                            .orElseThrow(() -> new ProductoNotFoundException(productId));
                    User user = userService.findUserById(userId);
                    return favoriteRepository.save(new Favorite(product, user));
                });
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
