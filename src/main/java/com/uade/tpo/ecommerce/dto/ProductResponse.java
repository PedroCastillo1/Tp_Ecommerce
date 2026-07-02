package com.uade.tpo.ecommerce.dto;

// ## DTO — Respuesta de Producto
// ##
// ## Representa cómo se serializa un producto en las respuestas de la API.
// ## No es una entidad de BD — es una "vista" del producto para el cliente.
// ##
// ## Diferencias vs la entidad Product:
// ##   - categories es Set<CategoryResponse> (solo id y name, sin los productos)
// ##     en lugar de Set<Category> (que causaría recursión infinita)
// ##   - No tiene métodos de Spring Security (getAuthorities, etc.)
// ##   - Es solo datos, sin lógica de negocio
// ##
// ## @AllArgsConstructor → constructor con todos los campos (usado en ProductService.toResponse())
// ## @NoArgsConstructor  → constructor vacío (requerido por Jackson para deserializar)

import java.math.BigDecimal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long                    id;
    private String                  name;
    private String                  description;
    private BigDecimal              price;
    private Integer                 stock;
    private String                  imageUrl;
    private Set<CategoryResponse>   categories; // ## categorías como DTO simple (id + name)
}
