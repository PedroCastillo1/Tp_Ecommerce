package com.uade.tpo.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {
    @Id
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String password;

    @ManyToMany(mappedBy = "users")
    private java.util.Set<Product> products;

}
