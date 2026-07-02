package com.uade.tpo.ecommerce.service;

// ## SERVICIO — Usuarios
// ##
// ## Lógica de negocio para la gestión de usuarios.
// ## Implementa UserDetailsService de Spring Security (requerido por el sistema de auth).
// ##
// ## IMPORTANTE: loadUserByUsername() es el método que Spring Security llama
// ## internamente para autenticar al usuario durante el login.
// ## Recibe el email (que usamos como username) y devuelve el User como UserDetails.
// ##
// ## NOTA: el método login() aquí es legacy y compara passwords en texto plano.
// ##       El login real del sistema usa AuthenticationService con BCrypt.

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.repository.IUserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    // ## Lista todos los usuarios de la DB
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ## Crea un usuario directamente (sin encriptar contraseña — legacy)
    // ## Para creación segura usar AuthenticationService.register()
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // ## Actualiza los datos de perfil de un usuario existente
    // ## Devuelve null si el usuario no existe
    @Override
    public User updateUser(Long id, String username, String email, String password, String nombre, String apellido) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(username);
            existingUser.setEmail(email);
            existingUser.setPassword(password);
            existingUser.setNombre(nombre);
            existingUser.setApellido(apellido);
            return userRepository.save(existingUser);
        }
        return null;
    }

    // ## Elimina todos los usuarios de la DB (usar con extremo cuidado en producción)
    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    // ## Elimina un usuario por su ID
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // ## Guarda o actualiza un usuario en la DB
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // ## Verifica si existe un usuario con el ID dado
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    // ## Busca un usuario por ID — devuelve null si no existe
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ## Busca un usuario por email — devuelve null si no existe
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ## Login legacy: compara passwords en TEXTO PLANO (no seguro)
    // ## Solo para endpoints legacy — el sistema real usa BCrypt via AuthenticationService
    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // ## REQUERIDO POR SPRING SECURITY: busca el usuario por email para autenticación
    // ## Es llamado automáticamente por AuthenticationManager durante el login
    // ## Lanza UsernameNotFoundException si no existe (Spring lo maneja como 401)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }
}
