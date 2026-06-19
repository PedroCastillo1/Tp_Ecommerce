package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Orden de Compra
// ##
// ## Representa una compra completada por un usuario.
// ## Se crea en CartService.checkout() al confirmar el carrito.
// ##
// ## Tabla DB: "ordenes"
// ##
// ## Relaciones:
// ##   user  → ManyToOne con User  (muchas órdenes pueden pertenecer a un usuario)
// ##   items → OneToMany con OrderItem (una orden tiene uno o más items)
// ##
// ## @JsonIgnore en user: evita serializar el objeto User completo en la respuesta
// ## (previene recursión infinita y evita exponer datos sensibles como el password)
// ##
// ## fetch = EAGER en items: los items se cargan automáticamente con la orden,
// ## sin necesitar una query extra (conveniente para mostrar el historial completo)

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ordenes")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ## El usuario que realizó la compra — no se serializa en JSON
    @ManyToOne
    @JsonIgnore
    private User user;

    // ## Los productos comprados en esta orden
    // ## CascadeType.ALL: si se borra la orden, se borran sus items
    // ## FetchType.EAGER: se cargan automáticamente con cada query de Order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    private BigDecimal total; // ## suma de (precio × cantidad) de todos los items

    private LocalDateTime fecha; // ## timestamp de cuándo se realizó la compra

    public Order() {}

    public Order(User user, List<OrderItem> items, BigDecimal total) {
        this.user  = user;
        this.items = items;
        this.total = total;
        this.fecha = LocalDateTime.now();
    }
}
