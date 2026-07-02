package com.uade.tpo.ecommerce.model;

// ## MODELO — Solicitud de Login
// ##
// ## DTO (Data Transfer Object) que representa el cuerpo del request de login.
// ## No es una entidad de BD (@Entity ausente) — solo estructura el JSON que llega.
// ##
// ## Usado por:
// ##   AuthenticationController.login() → /api/auth/login (con JWT)
// ##   UsuarioController.login()        → /api/usuarios/login (legacy, sin JWT)
// ##
// ## Lombok @Getter/@Setter genera automáticamente los métodos
// ## getEmail(), setEmail(), getPassword(), setPassword()

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;    // ## email del usuario (usado como identificador de login)
    private String password; // ## contraseña en texto plano (la encriptación la maneja el servicio)
}
