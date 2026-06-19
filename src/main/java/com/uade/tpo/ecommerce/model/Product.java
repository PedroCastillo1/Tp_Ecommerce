package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Producto
// ##
// ## Representa un producto del catálogo de la tienda.
// ## Tabla DB: "product" (nombre por defecto de JPA)
// ##
// ## Campos:
// ##   name        → nombre del producto
// ##   description → descripción larga
// ##   price       → precio (BigDecimal para precisión decimal exacta)
// ##   stock       → cantidad disponible, se descuenta en CartService.checkout()
// ##   imageUrl    → URL de la imagen (externa o local)
// ##
// ## Relación ManyToMany con Category via tabla intermedia "producto_categoria":
// ##   Un producto puede estar en muchas categorías (ej: "Electrónica" y "Oferta")
// ##   Una categoría puede tener muchos productos

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String     name;
    private String     description;
    private BigDecimal price;   // ## BigDecimal para evitar errores de punto flotante en precios
    private Integer    stock;   // ## se decrementa en checkout; 0 = sin stock
    private String     imageUrl;

    // ## Categorías del producto
    // ## Tabla intermedia "producto_categoria":
    // ##   producto_id  → FK a product.id
    // ##   categoria_id → FK a category.id
    @ManyToMany
    @JoinTable(
        name = "producto_categoria",
        joinColumns        = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Category> categories = new HashSet<>();
}
