package com.uade.tpo.ecommerce.service;

// ## SERVICIO — Autenticación
// ##
// ## Contiene la lógica de negocio para registrar e iniciar sesión.
// ## Es llamado por AuthenticationController.
// ##
// ## register(): valida unicidad → crea usuario → encripta password → genera JWT
// ## login():    valida credenciales via Spring Security → genera JWT con userId

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
    private PasswordEncoder passwordEncoder; // ## BCrypt — inyectado desde SecurityConfig

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ## Registra un usuario nuevo:
    // ##   1. Valida que el email y username no estén ya en uso
    // ##   2. Encripta la contraseña con BCrypt (nunca guardar en texto plano)
    // ##   3. Guarda el usuario con rol USER
    // ##   4. Genera y retorna un JWT para que el controller setee la cookie
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

        // ## Incluir el ID en el token para que el frontend pueda usarlo directamente
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        return jwtService.generateToken(extraClaims, user);
    }

    // ## Autentica un usuario con email y contraseña:
    // ##   1. authenticationManager.authenticate() delega en Spring Security
    // ##      que verifica la contraseña contra el hash BCrypt en la DB
    // ##   2. Si son válidas, busca el usuario y genera el JWT con su ID
    // ##   3. Si son inválidas, Spring lanza AuthenticationException → la capturamos
    public String login(String email, String password) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            User user = userRepository.findByEmail(email).orElseThrow();

            // ## Incluir el ID del usuario en el token (el frontend lo usa para las API calls)
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userId", user.getId());
            return jwtService.generateToken(extraClaims, user);
        } catch (Exception e) {
            throw new RuntimeException("Credenciales inválidas o error de servidor");
        }
    }
}
