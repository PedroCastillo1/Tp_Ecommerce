package com.uade.tpo.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.User;

@Service
public interface IUserService {
    public List<User> getAllUsers();
    public User createUser(User user);
    public User updateUser(Long id, long idnueva,String username,String password);
    public void deleteAllUsers();
    public void deleteUserById(Long id);
    public void saveUser(User user);
    public boolean existsById(Long id);
    public User findUserByid(Long id);
    

}
