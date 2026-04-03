package com.uade.tpo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.Product;

@Repository
public interface IProductosRepository extends JpaRepository<Product, Long> {
    
}
