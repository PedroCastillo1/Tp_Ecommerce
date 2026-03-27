package com.uade.tpo.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Product {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String price;

    @ManyToMany
    private Set<Category> categories;
}
