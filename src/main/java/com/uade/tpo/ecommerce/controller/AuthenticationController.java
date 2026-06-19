package com.uade.tpo.ecommerce.controller;

// ## CONTROLADOR — Autenticación
// ##
// ## Maneja el registro, login y logout de usuarios.
// ## JWT se envía y recibe como cookie HTTP-Only (no en headers ni en el body).
// ## Esto previene que JavaScript pueda robar el token (protección XSS).
// ##
// ## Endpoints:
// ##   POST /api/auth/register  → crea cuenta, setea cookie JWT
// ##   POST /api/auth/login     → autentica, setea cookie JWT
// ##   POST /api/auth/logout    → borra la cookie JWT (MaxAge=0)
// ##   GET  /api/auth/me        → devuelve datos del usuario autenticado actual

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.ecommerce.model.RegisterRequest;
import com.uade.tpo.ecommerce.model.LoginRequest;
import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    // ## Helper privado: configura y agrega la cookie JWT al response HTTP
    // ##   HttpOnly=true  → JavaScript no puede leer la cookie (previene XSS)
    // ##   Secure=false   → permitir HTTP en desarrollo (cambiar a true con HTTPS en producción)
    // ##   Path="/"       → la cookie se envía en todas las rutas del dominio
    // ##   MaxAge=86400   → expira en 1 día (mismo tiempo que el JWT)
    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ## cambiar a true en producción con HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // ## 1 día en segundos
        response.addCookie(cookie);
    }

    // ## POST /api/auth/register
    // ## Crea una cuenta nueva y setea la cookie JWT en el browser del cliente
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        try {
            String token = authService.register(request);
            setJwtCookie(response, token);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // ## Email o username ya en uso
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno al registrar.");
        }
    }

    // ## POST /api/auth/login
    // ## Autentica con email+password y setea la cookie JWT en el browser
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            setJwtCookie(response, token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email o contraseña incorrectos.");
        }
    }

    // ## POST /api/auth/logout
    // ## Invalida la sesión seteando la cookie JWT con MaxAge=0
    // ## El browser la borra inmediatamente al recibir esta respuesta
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // ## MaxAge=0 → el browser borra la cookie
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    // ## GET /api/auth/me
    // ## Devuelve los datos del usuario autenticado según la cookie JWT activa.
    // ## @AuthenticationPrincipal inyecta el User extraído por JwtAuthenticationFilter.
    // ## El frontend usa este endpoint al recargar la página para restaurar la sesión.
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        Map<String, Object> data = new HashMap<>();
        data.put("id",       user.getId());
        data.put("email",    user.getEmail());
        data.put("username", user.getNombre());
        return ResponseEntity.ok(data);
    }
}
