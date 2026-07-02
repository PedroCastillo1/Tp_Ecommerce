package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Usuarios (CRUD administrativo)
// ##
// ## Expone endpoints para gestión de usuarios.
// ## NOTA: el registro y login de usuarios se hace via AuthenticationController.
// ##        Este controller es para administración (listar, actualizar, eliminar).
// ##
// ## NOTA 2: el endpoint /login aquí es legacy (pre-JWT), ya no se usa en el frontend.
// ##         El login real está en /api/auth/login con cookie HTTP-Only.
// ##
// ## Endpoints:
// ##   GET    /api/usuarios           → listar todos los usuarios
// ##   GET    /api/usuarios/{id}      → obtener usuario por ID
// ##   GET    /api/usuarios/existe/{id} → verificar si existe un usuario
// ##   POST   /api/usuarios           → crear usuario (sin encriptar contraseña, legacy)
// ##   PUT    /api/usuarios/{id}      → actualizar datos del usuario
// ##   DELETE /api/usuarios/{id}      → eliminar usuario por ID
// ##   DELETE /api/usuarios           → eliminar todos los usuarios

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.ecommerce.model.LoginRequest;
import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.service.IUserService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUserService userService;

    // ## GET /api/usuarios — lista todos los usuarios del sistema
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ## GET /api/usuarios/existe/{id} — verifica si existe un usuario (devuelve true/false)
    @GetMapping("/existe/{id}")
    public boolean existsById(@PathVariable Long id) {
        return userService.existsById(id);
    }

    // ## GET /api/usuarios/{id} — obtiene un usuario específico por su ID
    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    // ## POST /api/usuarios — crea un usuario (legacy, sin BCrypt)
    // ## Para registro seguro usar /api/auth/register
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // ## DELETE /api/usuarios/{id} — elimina un usuario por ID
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    // ## DELETE /api/usuarios — elimina TODOS los usuarios (cuidado en producción)
    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    // ## PUT /api/usuarios/{id} — actualiza los datos del perfil de un usuario
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(
            id,
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getNombre(),
            user.getApellido()
        );
    }

    // ## POST /api/usuarios/login — login legacy SIN JWT (compara passwords en texto plano)
    // ## DEPRECATED: usar /api/auth/login con cookie HTTP-Only
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return user;
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}
