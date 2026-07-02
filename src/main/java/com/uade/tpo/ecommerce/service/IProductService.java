package com.uade.tpo.ecommerce.service;

// ## INTERFAZ — Contrato del Servicio de Productos
// ##
// ## Define las operaciones que debe implementar ProductService.
// ## Usa DTOs en lugar de la entidad directa para desacoplar la API del modelo.
// ##
// ## ProductRequest  → datos de entrada (lo que manda el cliente)
// ## ProductResponse → datos de salida (lo que devolvemos al cliente)

import java.util.List;

import com.uade.tpo.ecommerce.dto.ProductRequest;
import com.uade.tpo.ecommerce.dto.ProductResponse;

public interface IProductService {
    List<ProductResponse> getAllProducts();                           // ## listar todos
    List<ProductResponse> getAllProductsOrdered();                   // ## listar ordenados A-Z
    ProductResponse createProduct(ProductRequest request);           // ## crear producto
    ProductResponse updateProduct(Long id, ProductRequest request);  // ## actualizar producto
    void deleteProductById(Long id);                                 // ## eliminar por ID
    ProductResponse findProductById(Long id);                        // ## buscar por ID
    boolean existsById(Long id);                                     // ## verificar existencia
}
