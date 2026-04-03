package com.uade.tpo.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.repository.ICategoriaRepository;

public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Override
    public void deleteAllCategories() {
        categoriaRepository.deleteAll();
        
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoriaRepository.deleteById(id);
        
    }

    @Override
    public void saveCategory(Category category) {
        categoriaRepository.save(category);
    }

    @Override
    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }

    @Override
    public Category findCategoryByid(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoriaRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
            return categoriaRepository.save(category);
        
    }

    @Override
    public Category updateCategory(Long id, long idnueva, String name) {
        Category existingCategory = categoriaRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.setId(idnueva);
            existingCategory.setName(name);
            return categoriaRepository.save(existingCategory);
        }
        return null;
    }



 
    
}
