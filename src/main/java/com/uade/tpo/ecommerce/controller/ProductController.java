package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Productos
// ##
// ## Expone el CRUD completo de productos del catálogo.
// ## Los GET son públicos; POST, PUT y DELETE requieren autenticación.
// ## Usa DTOs (ProductRequest/ProductResponse) en lugar de la entidad directa
// ## para desacoplar la API del modelo interno de la DB.
// ##
// ## Endpoints:
// ##   GET    /api/productos              → listar todos (opcional: ?ordered=true para orden A-Z)
// ##   GET    /api/productos/{id}         → obtener producto por ID
// ##   POST   /api/productos              → crear producto nuevo (con validación @Valid)
// ##   PUT    /api/productos/{id}         → actualizar producto existente
// ##   DELETE /api/productos/{id}         → eliminar producto

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.ecommerce.dto.ProductRequest;
import com.uade.tpo.ecommerce.dto.ProductResponse;
import com.uade.tpo.ecommerce.service.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    @Autowired
    private IProductService productService;

    // ## GET /api/productos
    // ## ?ordered=false (default) → orden de inserción en DB
    // ## ?ordered=true           → ordenados alfabéticamente por nombre
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false, defaultValue = "false") boolean ordered) {
        List<ProductResponse> productos = ordered
                ? productService.getAllProductsOrdered()
                : productService.getAllProducts();
        return ResponseEntity.ok(productos);
    }

    // ## GET /api/productos/{id}
    // ## Lanza ProductoNotFoundException (404) si no existe
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    // ## POST /api/productos
    // ## @Valid dispara la validación de ProductRequest antes de ejecutar el método
    // ## Devuelve 201 CREATED con el producto creado
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ## PUT /api/productos/{id}
    // ## Actualiza todos los campos del producto (reemplazo completo, no parcial)
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // ## DELETE /api/productos/{id}
    // ## Devuelve 204 NO CONTENT si se eliminó correctamente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
