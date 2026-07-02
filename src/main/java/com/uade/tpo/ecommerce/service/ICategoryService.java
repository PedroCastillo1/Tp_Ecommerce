package com.uade.tpo.ecommerce.service;

// ## INTERFAZ — Contrato del Servicio de Categorías
// ##
// ## Define las operaciones que debe implementar CategoryService.
// ## Incluye CRUD completo: crear, leer, actualizar y eliminar categorías.

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.Category;

@Service
public interface ICategoryService {

    void deleteAllCategories();                           // ## eliminar todas las categorías
    void deleteCategoryById(Long id);                     // ## eliminar una categoría por ID
    void saveCategory(Category category);                 // ## guardar/actualizar categoría
    boolean existsById(Long id);                          // ## verificar si existe por ID
    Category findCategoryByid(Long id);                   // ## buscar categoría por ID
    List<Category> getAllCategories();                     // ## listar todas las categorías
    Category createCategory(Category category);           // ## crear categoría nueva
    Category updateCategory(Long id, String name);        // ## actualizar nombre de categoría
}
