package com.uade.tpo.ecommerce.service;

import java.util.List;

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;

public class ProductService implements IProductService {
        @Override
        public void saveProduct(Product product) {
            // TODO Auto-generated method stub
            
        }
    
        @Override
        public boolean existsById(Long id) {
            // TODO Auto-generated method stub
            return false;
        }
    
        @Override
        public Product findProductByid(Long id) {
            // TODO Auto-generated method stub
            return null;
        }
    
        @Override
        public List<Product> getAllProducts() {
            // TODO Auto-generated method stub
            return null;
        }
    
        @Override
        public Product createProduct(Product product) {
            // TODO Auto-generated method stub
            return null;
        }
    
        @Override
        public Product updateProduct(Long id, long idnueva, String name, String description, double price, int stock,
                Category category) {
            // TODO Auto-generated method stub
            return null;
        }
    
        @Override
        public void deleteAllProducts() {
            // TODO Auto-generated method stub
            
        }
    
        @Override
        public void deleteProductById(Long id) {
            // TODO Auto-generated method stub
    
        }
}
