package com.uade.tpo.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.model.User;
import com.uade.tpo.ecommerce.repository.IUserRepository;

@Service
public class UserService implements IUserService  {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> ListaUsers = userRepository.findAll();
        return ListaUsers;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, long idnueva,String username,String password) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setId(idnueva);
            existingUser.setUsername(username);
            existingUser.setPassword(password);
            return userRepository.save(existingUser);
        }
        return null;
    }


    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(Long id) {
       userRepository.deleteById(id);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }


    @Override
    public User findUserByid(Long id) {
        return userRepository.findById(id).orElse(null);
    } 
    


    
}
