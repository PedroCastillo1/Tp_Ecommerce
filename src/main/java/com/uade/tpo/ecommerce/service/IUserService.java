package com.uade.tpo.ecommerce.service;

// ## INTERFAZ — Contrato del Servicio de Usuarios
// ##
// ## Extiende UserDetailsService de Spring Security.
// ## Esto es OBLIGATORIO para integrar con el sistema de autenticación de Spring:
// ## Spring Security llama a loadUserByUsername() (heredado de UserDetailsService)
// ## durante el proceso de autenticación para verificar credenciales.
// ##
// ## El parámetro "username" que recibe loadUserByUsername() es en realidad el EMAIL,
// ## porque en User.getUsername() retornamos el email.

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.uade.tpo.ecommerce.model.User;

public interface IUserService extends UserDetailsService {
    List<User> getAllUsers();                                         // ## listar todos los usuarios
    User createUser(User user);                                      // ## crear usuario (legacy)
    User updateUser(Long id, String username, String email,
                   String password, String nombre, String apellido); // ## actualizar perfil
    void deleteAllUsers();                                           // ## eliminar todos (admin)
    void deleteUserById(Long id);                                    // ## eliminar por ID
    void saveUser(User user);                                        // ## guardar usuario
    boolean existsById(Long id);                                     // ## verificar existencia
    User findUserById(Long id);                                      // ## buscar por ID
    User findUserByEmail(String email);                              // ## buscar por email
    User login(String email, String password);                       // ## login legacy (sin BCrypt)
    // ## loadUserByUsername() heredado de UserDetailsService → implementado en UserService
}
