package com.uade.tpo.ecommerce.exception;

// ## EXCEPCIÓN — Producto No Encontrado
// ##
// ## Se lanza cuando se intenta acceder a un producto que no existe en la DB.
// ## Es capturada por GlobalExceptionHandler que la convierte en HTTP 404.
// ##
// ## Dos constructores: por ID (más común) o por mensaje personalizado.

public class ProductoNotFoundException extends RuntimeException {

    // ## Lanzada cuando se busca un producto por ID y no existe
    public ProductoNotFoundException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }

    // ## Lanzada con un mensaje personalizado cuando aplique
    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
