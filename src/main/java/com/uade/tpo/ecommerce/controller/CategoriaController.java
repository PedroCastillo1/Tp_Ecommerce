package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Categorías
// ##
// ## Expone endpoints REST para el CRUD de categorías de productos.
// ## Las rutas GET son públicas (configurado en SecurityConfig).
// ## Las rutas POST, PUT y DELETE requieren autenticación.
// ##
// ## Endpoints:
// ##   GET    /categoria         → listar todas las categorías
// ##   GET    /categoria/{id}    → obtener una categoría por ID
// ##   POST   /categoria         → crear una categoría nueva
// ##   PUT    /categoria/{id}    → actualizar el nombre de una categoría
// ##   DELETE /categoria/{id}    → eliminar una categoría

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.ecommerce.model.Category;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private com.uade.tpo.ecommerce.service.ICategoryService categoryService;

    // ## GET /categoria
    // ## Lista todas las categorías disponibles (Electrónica, Ropa, Calzado, etc.)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // ## GET /categoria/{id}
    // ## Devuelve una categoría específica por su ID
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryByid(id);
    }

    // ## POST /categoria
    // ## Crea una categoría nueva con el nombre del body
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // ## PUT /categoria/{id}
    // ## Actualiza el nombre de una categoría existente
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category.getName());
    }

    // ## DELETE /categoria/{id}
    // ## Elimina una categoría por su ID
    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}
