package com.uade.tpo.ecommerce.controller;

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

    // ------------------------------------------------------

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/existe/{id}")
    public boolean existsById(@PathVariable Long id) {
        return userService.existsById(id);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
    //------------------------------------------------------

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }


    // ------------------------------------------------------

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    // ------------------------------------------------------

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


    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return user;
        }

        throw new RuntimeException("Credenciales inválidas");
    }
}