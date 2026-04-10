package com.uade.tpo.ecommerce.exception;

public class CategoriaYaExisteException extends RuntimeException {
    public CategoriaYaExisteException(String nombre) {
        super("Ya existe una categoría con el nombre: " + nombre);
    }
}
