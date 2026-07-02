package com.uade.tpo.ecommerce.model;

// ## ENUMERACIÓN — Sexo del Usuario
// ##
// ## Opciones disponibles para el campo sexo en el registro de usuario.
// ## Se guarda como String en la DB (via @Enumerated(EnumType.STRING) en User).
// ##
// ## Requerido en RegisterRequest con validación @NotNull.

public enum Sexo {
    MASCULINO,  // ## opción masculino
    FEMENINO,   // ## opción femenino
    OTRO        // ## otras identidades
}
