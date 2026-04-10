package com.uade.tpo.ecommerce.exception;

public class ProductoYaExisteException extends RuntimeException {
    public ProductoYaExisteException(String nombre) {
        super("Ya existe un producto con el nombre: " + nombre);
    }

    public ProductoYaExisteException(Long id) {
        super("Ya existe un producto con ID: " + id);
    }
}
