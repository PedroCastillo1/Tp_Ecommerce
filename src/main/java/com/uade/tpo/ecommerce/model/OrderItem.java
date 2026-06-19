package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Item de una Orden
// ##
// ## Representa un producto dentro de una orden completada.
// ## Guarda el precioUnitario al momento de la compra (snapshot del precio).
// ## Esto es importante porque el precio del producto puede cambiar después.
// ##
// ## Tabla DB: "orden_items"
// ##
// ## Relaciones:
// ##   order   → ManyToOne con Order   (un item pertenece a una orden)
// ##   product → ManyToOne con Product (qué producto se compró)
// ##
// ## @JsonIgnore en order: evita recursión infinita al serializar
// ## (Order tiene List<OrderItem> y cada OrderItem tendría Order → ciclo infinito)

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orden_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ## La orden a la que pertenece — @JsonIgnore para evitar recursión
    @ManyToOne
    @JsonIgnore
    private Order order;

    // ## El producto comprado — se serializa completo para mostrar nombre/imagen en historial
    @ManyToOne
    private Product product;

    private Integer    quantity;       // ## cuántas unidades se compraron
    private BigDecimal precioUnitario; // ## precio al momento de la compra (no cambia con el tiempo)

    public OrderItem() {}

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal precioUnitario) {
        this.order          = order;
        this.product        = product;
        this.quantity       = quantity;
        this.precioUnitario = precioUnitario;
    }
}
