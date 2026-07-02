package com.uade.tpo.ecommerce.exception;

// ## MANEJADOR GLOBAL DE EXCEPCIONES
// ##
// ## Centraliza el manejo de errores de TODA la aplicación.
// ## @ControllerAdvice intercepta excepciones lanzadas por cualquier controller
// ## y las convierte en respuestas HTTP con el código de estado apropiado.
// ##
// ## Sin este handler, Spring devolvería un 500 genérico para cualquier error.
// ## Con él, cada tipo de error tiene su HTTP status code correcto:
// ##   400 Bad Request → validaciones fallidas, runtime exceptions de negocio
// ##   404 Not Found   → recursos no encontrados (producto, categoría)
// ##   409 Conflict    → recursos que ya existen (nombre duplicado)
// ##   500 Server Error → errores inesperados
// ##
// ## Todos los handlers devuelven Map<String, String> con clave "error"
// ## para que el frontend pueda mostrar el mensaje directamente.

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ## 400 BAD REQUEST — errores de validación de Bean Validation (@NotBlank, @Email, etc.)
    // ## Ocurre cuando el body del request no cumple las anotaciones de validación
    // ## Devuelve un mapa con todos los campos inválidos y sus mensajes de error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName    = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // ## 404 NOT FOUND — producto no encontrado en la DB
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductoNotFound(ProductoNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ## 409 CONFLICT — ya existe un producto con ese nombre o ID
    @ExceptionHandler(ProductoYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleProductoYaExiste(ProductoYaExisteException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // ## 404 NOT FOUND — categoría no encontrada en la DB
    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoriaNotFound(CategoriaNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ## 409 CONFLICT — ya existe una categoría con ese nombre
    @ExceptionHandler(CategoriaYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleCategoriaYaExiste(CategoriaYaExisteException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // ## 400 BAD REQUEST — errores de lógica de negocio (ej: "Stock insuficiente", "Credenciales inválidas")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ## 500 INTERNAL SERVER ERROR — cualquier otro error no previsto
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
