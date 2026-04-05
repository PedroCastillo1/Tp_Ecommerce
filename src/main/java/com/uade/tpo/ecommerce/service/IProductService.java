package com.uade.tpo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;

public interface IProductService {
    List<Product> getAllProducts();
    List<Product> getAllProductsOrdered();
    Product createProduct(Product product);
    Product updateProduct(Long id, String name, String description, BigDecimal price, Integer stock, String imageUrl, Set<Category> categories);
    void deleteProductById(Long id);
    Product findProductById(Long id);
    boolean existsById(Long id);
}