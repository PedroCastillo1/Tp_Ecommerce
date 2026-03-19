package com.uade.tpo.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/usuarios")
@RestController
public class UsuarioController {

    @GetMapping("/Auriculares")
    public String getTraemeTodosLosUsuariosConAuriculares(){
        return "Hola Bro, te traje todos los usuarios con Auriculares";
    }

    @GetMapping()
    public String getUsuarios(){
        return "Hola Bro, te traje todos los usuarios";
    }

    @GetMapping("/{id}")
    public String getUserPorId(@RequestParam long id){
        return "Hola Bro, te traje el usuario con id: " + id;
    }

    @GetMapping("/{email}")
    public String getUserByEmail(@RequestParam String email){
        if (email == null || email.isEmpty()) {
            return "El email no puede ser nulo o vacío";
        }
        return "Hola Bro, te traje el usuario con email: " + email;
    }



}
