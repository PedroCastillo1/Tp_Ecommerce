package com.uade.tpo.ecommerce.exception;

// ## EXCEPCIÓN — Producto Ya Existe
// ##
// ## Se lanza cuando se intenta crear un producto que ya existe (por nombre o ID).
// ## Es capturada por GlobalExceptionHandler que la convierte en HTTP 409 Conflict.

public class ProductoYaExisteException extends RuntimeException {

    // ## Lanzada cuando ya existe un producto con el mismo nombre
    public ProductoYaExisteException(String nombre) {
        super("Ya existe un producto con el nombre: " + nombre);
    }

    // ## Lanzada cuando ya existe un producto con el mismo ID
    public ProductoYaExisteException(Long id) {
        super("Ya existe un producto con ID: " + id);
    }
}
