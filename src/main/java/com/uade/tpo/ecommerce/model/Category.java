package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Categoría de Producto
// ##
// ## Representa una categoría para clasificar productos (ej: Electrónica, Ropa).
// ## Tabla DB: "Categoria"
// ##
// ## Relación ManyToMany inversa con Product:
// ##   mappedBy = "categories" → Product es el dueño de la relación
// ##   (la tabla intermedia "producto_categoria" se define en Product)
// ##
// ## @JsonIgnore en products: evita recursión infinita al serializar
// ## (Category → products → Product → categories → Category → ...)

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Categoria")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ## Nombre único de la categoría (ej: "Electrónica", "Ropa")
    @Column(unique = true)
    private String name;

    // ## Lista de productos de esta categoría — @JsonIgnore para evitar recursión
    // ## mappedBy indica que la tabla intermedia está definida en Product
    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private java.util.Set<Product> products = new java.util.HashSet<>();
}
