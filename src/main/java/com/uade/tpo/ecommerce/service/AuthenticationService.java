package com.uade.tpo.ecommerce.service;

import java.util.HashMap;
import java.util.Map;

import com.uade.tpo.ecommerce.model.RegisterRequest;
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

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        User user = new User();
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setFechaNacimiento(request.getFechaNacimiento());
        user.setSexo(request.getSexo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        return jwtService.generateToken(extraClaims, user);
    }

    public String login(String email, String password) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            User user = userRepository.findByEmail(email).orElseThrow();

            // Incluimos el ID del usuario en el token
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userId", user.getId());
            return jwtService.generateToken(extraClaims, user);
        } catch (Exception e) {
            throw new RuntimeException("Credenciales inválidas o error de servidor");
        }
    }
}
