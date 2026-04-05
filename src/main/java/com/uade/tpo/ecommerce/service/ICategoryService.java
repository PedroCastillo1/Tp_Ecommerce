package com.uade.tpo.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.Category;

@Service
public interface ICategoryService {

    public void deleteAllCategories();
    public void deleteCategoryById(Long id);
    public void saveCategory(Category category);
    public boolean existsById(Long id);
    public Category findCategoryByid(Long id);
    public List<Category> getAllCategories();
    public Category createCategory(Category category);
    public Category updateCategory(Long id,String name);

    
}
