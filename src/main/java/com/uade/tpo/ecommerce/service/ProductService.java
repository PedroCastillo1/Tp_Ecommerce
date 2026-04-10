package com.uade.tpo.ecommerce.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.dto.CategoryResponse;
import com.uade.tpo.ecommerce.dto.ProductRequest;
import com.uade.tpo.ecommerce.dto.ProductResponse;
import com.uade.tpo.ecommerce.exception.CategoriaNotFoundException;
import com.uade.tpo.ecommerce.exception.ProductoNotFoundException;
import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.repository.ICategoriaRepository;
import com.uade.tpo.ecommerce.repository.IProductosRepository;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductosRepository productRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    // ── Conversión entidad → DTO ──────────────────────────────────────────────
    private ProductResponse toResponse(Product product) {
        Set<CategoryResponse> categorias = product.getCategories().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toSet());
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                categorias);
    }

    // ── Conversión DTO → entidad ─────────────────────────────────────────────
    private Set<Category> resolveCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Category> categories = new HashSet<>();
        for (Long catId : categoryIds) {
            Category cat = categoriaRepository.findById(catId)
                    .orElseThrow(() -> new CategoriaNotFoundException(catId));
            categories.add(cat);
        }
        return categories;
    }

    // ── Métodos públicos ─────────────────────────────────────────────────────

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsOrdered() {
        return productRepository.findAll().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategories(resolveCategories(request.getCategoryIds()));
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setImageUrl(request.getImageUrl());
        existing.setCategories(resolveCategories(request.getCategoryIds()));
        return toResponse(productRepository.save(existing));
    }

    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductoNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        return toResponse(product);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
