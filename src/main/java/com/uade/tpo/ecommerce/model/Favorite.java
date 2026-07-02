package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Favorito
// ##
// ## Representa la relación entre un usuario y un producto que marcó como favorito.
// ## Funciona como tabla de asociación con ID propio.
// ##
// ## Tabla DB: "favorite" (nombre por defecto de JPA)
// ##
// ## Relaciones:
// ##   product → ManyToOne con Product (el producto favorito — se serializa completo)
// ##   user    → ManyToOne con User    (el dueño del favorito — @JsonIgnore)
// ##
// ## @JsonIgnore en user: evita recursión y no expone datos del usuario
// ## al devolver la lista de favoritos en la API

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ## El producto que es favorito — se serializa con nombre, precio e imagen
    @ManyToOne
    private Product product;

    // ## El usuario dueño del favorito — @JsonIgnore para no incluirlo en la respuesta
    @JsonIgnore
    @ManyToOne
    private User user;

    public Favorite() {}

    public Favorite(Product product, User user) {
        this.product = product;
        this.user    = user;
    }
}
