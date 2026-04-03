package com.uade.tpo.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.service.IUserService;


@RequestMapping("/api/usuarios")
@RestController

public class UsuarioController {
    @Autowired
    private IUserService userService;

    @GetMapping("/mostrarUsuarios")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/encontrarUsuario/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserByid(id);
    }

    @PostMapping("/crearUsuario")
    public User createUser(@RequestBody User user) {
        System.out.println("Usuario creado: " + user.getUsername());
        return userService.createUser(user);
    }

    @GetMapping("/actualizarUsuario/{id}/{idnueva}/{username}/{password}")
    public User updateUser(@PathVariable Long id, @PathVariable long idnueva,@PathVariable String username,@PathVariable String password) {
        return userService.updateUser(id, idnueva,username,password);
    }


    @GetMapping("/eliminarTodosUsuarios")
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @GetMapping("/eliminarUsuarioPorId/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping ("/existeUsuario/{id}")
    public boolean existsById(@PathVariable Long id) {
        return userService.existsById(id);
    }

    @GetMapping("/guardarUsuario")
    public void saveUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping("/encontrarUsuarioPorId/{id}")
    public User findUserByid(@PathVariable Long id) {
        return userService.findUserByid(id);
    }

    
    




}
