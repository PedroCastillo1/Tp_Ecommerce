package com.uade.tpo.ecommerce.model;

// ## MODELO — Solicitud de Registro
// ##
// ## DTO que representa el cuerpo del request de registro de un usuario nuevo.
// ## No es una entidad de BD (@Entity ausente) — solo estructura el JSON de entrada.
// ##
// ## Incluye validaciones de Bean Validation (@NotBlank, @Email, @Size, @Past):
// ##   - Si alguna validación falla, Spring lanza MethodArgumentNotValidException
// ##   - GlobalExceptionHandler la captura y devuelve 400 con los errores detallados
// ##
// ## @JsonFormat en fechaNacimiento: le dice a Jackson cómo deserializar la fecha
// ##   (el frontend debe enviar el formato "yyyy-MM-dd", ej: "1990-05-15")

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 3, message = "La contraseña debe tener al menos 3 caracteres")
    private String password; // ## se encripta con BCrypt en AuthenticationService.register()

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @JsonFormat(pattern = "yyyy-MM-dd") // ## formato esperado del frontend: "1990-05-15"
    private LocalDate fechaNacimiento;

    @NotNull(message = "El sexo es obligatorio")
    private Sexo sexo; // ## MASCULINO, FEMENINO u OTRO
}
