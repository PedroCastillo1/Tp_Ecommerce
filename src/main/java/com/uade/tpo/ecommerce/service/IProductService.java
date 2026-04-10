package com.uade.tpo.ecommerce.service;

import java.util.List;

import com.uade.tpo.ecommerce.dto.ProductRequest;
import com.uade.tpo.ecommerce.dto.ProductResponse;

public interface IProductService {
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getAllProductsOrdered();
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProductById(Long id);
    ProductResponse findProductById(Long id);
    boolean existsById(Long id);
}
