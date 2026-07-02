package com.uade.tpo.ecommerce.exception;

// ## EXCEPCIÓN — Categoría No Encontrada
// ##
// ## Se lanza cuando se intenta acceder a una categoría que no existe en la DB.
// ## Es capturada por GlobalExceptionHandler que la convierte en HTTP 404.
// ##
// ## Extiende RuntimeException (unchecked) → no necesita declararse con throws.
// ## Tiene dos constructores: por ID (para búsquedas por ID) y por nombre.

public class CategoriaNotFoundException extends RuntimeException {

    // ## Lanzada cuando se busca una categoría por ID y no existe
    public CategoriaNotFoundException(Long id) {
        super("Categoría no encontrada con ID: " + id);
    }

    // ## Lanzada cuando se busca una categoría por nombre y no existe
    public CategoriaNotFoundException(String nombre) {
        super("Categoría no encontrada con nombre: " + nombre);
    }
}
