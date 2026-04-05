package com.uade.tpo.ecommerce.service;

import java.util.List;

import com.uade.tpo.ecommerce.model.User;

public interface IUserService {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, String username, String email, String password, String nombre, String apellido);
    void deleteAllUsers();
    void deleteUserById(Long id);
    void saveUser(User user);
    boolean existsById(Long id);
    User findUserById(Long id);
    User findUserByEmail(String email);
    User login(String email, String password);
}