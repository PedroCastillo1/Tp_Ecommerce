package com.uade.tpo.ecommerce.model;

// ## ENUMERACIÓN — Roles de Usuario
// ##
// ## Define los roles posibles para un usuario en el sistema.
// ## Se guarda como String en la DB (gracias a @Enumerated(EnumType.STRING) en User).
// ##
// ## USER  → usuario estándar (puede comprar, agregar favoritos, ver su perfil)
// ## ADMIN → administrador (puede crear/editar/eliminar productos y categorías)
// ##
// ## SecurityConfig usa este rol para autorizar acceso a rutas protegidas
// ## via getAuthorities() → "ROLE_USER" o "ROLE_ADMIN"

public enum Role {
    USER,   // ## rol por defecto para usuarios registrados
    ADMIN   // ## rol para administradores del sistema
}
