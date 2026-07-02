package com.uade.tpo.ecommerce.service;

// ## SERVICIO — Categorías
// ##
// ## Lógica de negocio para gestionar las categorías de productos.
// ## Es llamado por CategoriaController.
// ## Las categorías se usan para clasificar los productos (Electrónica, Ropa, etc.)
// ## y se relacionan con Product via tabla intermedia "producto_categoria".

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.repository.ICategoriaRepository;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    // ## Elimina todas las categorías de la DB (usar con cuidado)
    @Override
    public void deleteAllCategories() {
        categoriaRepository.deleteAll();
    }

    // ## Elimina una categoría por su ID
    @Override
    public void deleteCategoryById(Long id) {
        categoriaRepository.deleteById(id);
    }

    // ## Guarda (crea o actualiza) una categoría en la DB
    @Override
    public void saveCategory(Category category) {
        categoriaRepository.save(category);
    }

    // ## Verifica si existe una categoría con el ID dado (true/false)
    @Override
    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }

    // ## Busca una categoría por ID — devuelve null si no existe
    @Override
    public Category findCategoryByid(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // ## Lista todas las categorías disponibles en la DB
    @Override
    public List<Category> getAllCategories() {
        return categoriaRepository.findAll();
    }

    // ## Crea una categoría nueva y la guarda en la DB
    @Override
    public Category createCategory(Category category) {
        return categoriaRepository.save(category);
    }

    // ## Actualiza el nombre de una categoría existente
    // ## Devuelve null si la categoría no existe (podría mejorarse con excepción)
    @Override
    public Category updateCategory(Long id, String name) {
        Category existingCategory = categoriaRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.setName(name);
            return categoriaRepository.save(existingCategory);
        }
        return null;
    }
}
