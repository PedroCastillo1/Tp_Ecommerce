package com.uade.tpo.ecommerce.dto;

// ## DTO — Respuesta de Categoría
// ##
// ## Objeto de transferencia que representa cómo se serializa una categoría
// ## cuando es parte de la respuesta de un producto (dentro de ProductResponse).
// ##
// ## Solo expone id y name — evita devolver la lista de productos de la categoría
// ## (lo que causaría recursión infinita con @ManyToMany).
// ##
// ## @AllArgsConstructor de Lombok genera el constructor con todos los campos
// ## usado en ProductService.toResponse()

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private Long   id;   // ## ID de la categoría
    private String name; // ## nombre de la categoría (ej: "Electrónica")
}
