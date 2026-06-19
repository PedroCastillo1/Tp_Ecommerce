package com.uade.tpo.ecommerce.model;

// ## ENTIDAD — Usuario
// ##
// ## Representa un usuario registrado en el sistema.
// ## Implementa UserDetails de Spring Security para integrarse
// ## automáticamente con el sistema de autenticación.
// ##
// ## Tabla DB: "usuarios"
// ##
// ## IMPORTANTE: getUsername() devuelve el EMAIL (no el campo username del perfil).
// ## Spring Security usa getUsername() como identificador → el JWT guarda el email.
// ## El campo "nombre" es el nombre real del usuario (distinto de username/alias).
// ##
// ## @JsonIgnore en password y métodos de UserDetails:
// ##   - Evita exponer el hash de la contraseña en respuestas JSON
// ##   - Oculta las propiedades internas de Spring Security (authorities, etc.)

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // ## alias/nombre de usuario (único)

    @Column(unique = true, nullable = false)
    private String email; // ## email (único, usado como identificador de login)

    // ## Contraseña guardada como hash BCrypt — @JsonIgnore protege el hash
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String    nombre;
    private String    apellido;
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo; // ## MASCULINO, FEMENINO, OTRO

    @Enumerated(EnumType.STRING)
    private Role role; // ## USER o ADMIN

    public User() {}

    // ## Spring Security usa esto para control de acceso por rol
    // ## Genera autoridad "ROLE_USER" o "ROLE_ADMIN" según el campo role
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        try {
            return List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role.name() : "USER")));
        } catch (Exception e) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    // ## IMPORTANTE: Spring Security usa getUsername() para identificar usuarios.
    // ## Retornamos el EMAIL porque es el campo que usamos para el login.
    // ## Coexiste con Lombok @Getter para "nombre" (getNombre()) sin conflicto.
    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    // ## Métodos requeridos por UserDetails — todos retornan true (sin lógica de bloqueo)
    @JsonIgnore @Override public boolean isAccountNonExpired()     { return true; }
    @JsonIgnore @Override public boolean isAccountNonLocked()      { return true; }
    @JsonIgnore @Override public boolean isCredentialsNonExpired() { return true; }
    @JsonIgnore @Override public boolean isEnabled()               { return true; }
}
