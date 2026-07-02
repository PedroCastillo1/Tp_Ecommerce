package com.uade.tpo.ecommerce.dto;

// ## DTO — Solicitud de Creación/Actualización de Producto
// ##
// ## Representa el cuerpo JSON que recibe ProductController en POST y PUT.
// ## No es una entidad de BD — solo estructura y valida los datos de entrada.
// ##
// ## Validaciones con Bean Validation:
// ##   @NotBlank     → campo de texto requerido (no vacío, no solo espacios)
// ##   @NotNull      → campo numérico requerido
// ##   @DecimalMin   → precio debe ser mayor a 0
// ##   @Min(0)       → stock no puede ser negativo
// ##
// ## categoryIds: Set de IDs de categorías a asociar al producto.
// ## ProductService los convierte a entidades Category via resolveCategories().

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    private String description; // ## descripción opcional

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private String imageUrl; // ## URL de imagen (opcional)

    // ## IDs de categorías a asociar — ProductService los resuelve a entidades Category
    private Set<Long> categoryIds;
}
