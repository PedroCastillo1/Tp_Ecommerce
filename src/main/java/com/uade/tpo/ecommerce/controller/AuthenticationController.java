package com.uade.tpo.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.ecommerce.model.RegisterRequest;
import com.uade.tpo.ecommerce.model.LoginRequest;
import com.uade.tpo.ecommerce.service.AuthenticationService;
import jakarta.validation.Valid; // Asegurate de tener este import

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) { // <-- EL @Valid VA ACÁ
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}