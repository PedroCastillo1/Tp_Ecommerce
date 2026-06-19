package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Item del Carrito
// ##
// ## Representa un producto que un usuario tiene en su carrito de compras.
// ## Funciona como tabla de asociación entre User y Product con cantidad adicional.
// ##
// ## Tabla DB: "cart_item" (nombre por defecto de JPA)
// ##
// ## Relaciones:
// ##   product → ManyToOne con Product (qué producto está en el carrito)
// ##   user    → ManyToOne con User    (a quién pertenece este item)
// ##
// ## @JsonIgnore en user: el frontend no necesita el objeto User en la respuesta
// ## del carrito (evita recursión y exponer datos innecesarios del usuario)

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ## El producto en el carrito — se serializa completo (nombre, precio, imagen)
    @ManyToOne
    private Product product;

    // ## El dueño del carrito — @JsonIgnore para no incluirlo en el JSON de respuesta
    @JsonIgnore
    @ManyToOne
    private User user;

    private Integer quantity; // ## cantidad de unidades de este producto

    public CartItem() {}

    public CartItem(Product product, User user, Integer quantity) {
        this.product  = product;
        this.user     = user;
        this.quantity = quantity;
    }
}
