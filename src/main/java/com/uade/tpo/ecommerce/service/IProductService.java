package com.uade.tpo.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;

@Service
public interface  IProductService {
    public void saveProduct(Product product);
    public boolean existsById(Long id);
    public Product findProductByid(Long id);
    public List<Product> getAllProducts();
    public Product createProduct(Product product);
    public Product updateProduct(Long id, long idnueva,String name, String description, double price, int stock, Category category);
    public void deleteAllProducts();
    public void deleteProductById(Long id);
    
    
}
