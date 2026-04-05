package com.uade.tpo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.repository.IProductosRepository;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductosRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsOrdered() {
        return productRepository.findAll().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, String name, String description, BigDecimal price, Integer stock, String imageUrl, Set<Category> categories) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(name);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price);
            existingProduct.setStock(stock);
            existingProduct.setImageUrl(imageUrl);
            existingProduct.setCategories(categories);
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}