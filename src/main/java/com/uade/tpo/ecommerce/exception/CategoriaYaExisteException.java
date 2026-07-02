package com.uade.tpo.ecommerce.exception;

// ## EXCEPCIÓN — Categoría Ya Existe
// ##
// ## Se lanza cuando se intenta crear una categoría con un nombre que ya está en uso.
// ## Es capturada por GlobalExceptionHandler que la convierte en HTTP 409 Conflict.

public class CategoriaYaExisteException extends RuntimeException {

    // ## Lanzada cuando ya existe una categoría con el mismo nombre
    public CategoriaYaExisteException(String nombre) {
        super("Ya existe una categoría con el nombre: " + nombre);
    }
}
