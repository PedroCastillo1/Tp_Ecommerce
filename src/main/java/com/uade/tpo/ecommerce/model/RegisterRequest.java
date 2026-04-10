package com.uade.tpo.ecommerce.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String username;
}