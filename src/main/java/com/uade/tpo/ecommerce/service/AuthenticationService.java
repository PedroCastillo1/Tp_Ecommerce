package com.uade.tpo.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.model.Role;
import com.uade.tpo.ecommerce.repository.IUserRepository;
import com.uade.tpo.ecommerce.security.JwtService;

@Service
public class AuthenticationService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(User request) {
        try {
            User user = new User();
            user.setNombre(request.getNombre());
            user.setApellido(request.getApellido());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptamos la clave
            user.setRole(Role.USER); // Rol por defecto

            userRepository.save(user);
            return jwtService.generateToken(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public String login(String email, String password) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            User user = userRepository.findByEmail(email).orElseThrow();
            return jwtService.generateToken(user);
        } catch (Exception e) {
            throw new RuntimeException("Credenciales invÃ¡lidas o error de servidor");
        }
    }
}