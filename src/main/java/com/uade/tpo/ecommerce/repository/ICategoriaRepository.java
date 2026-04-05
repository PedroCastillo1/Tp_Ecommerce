package com.uade.tpo.ecommerce.repository;

//import java.util.Locale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.model.Category;

@Repository
public interface  ICategoriaRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    
}
