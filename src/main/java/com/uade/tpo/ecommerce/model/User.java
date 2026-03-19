package com.uade.tpo.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

@Entity
public class User {
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String password;


}
