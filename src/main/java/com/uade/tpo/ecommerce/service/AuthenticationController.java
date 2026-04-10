package com.uade.tpo.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.ecommerce.model.RegisterRequest;
import com.uade.tpo.ecommerce.model.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        try {
            return authService.register(request);
        } catch (Exception e) {
            return "Error en el registro: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        try {
            return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (Exception e) {
            return "Error en el login: " + e.getMessage();
        }
    }
}