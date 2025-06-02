package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user, String confirmPassword) throws Exception {
        // Validate password match
        if (!user.getPassword().equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
        }

        // Validate password strength
        if (!isPasswordValid(user.getPassword())) {
            throw new Exception("Password does not meet requirements");
        }

        // Check if username already exists
        if (isUsernameExists(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String username, String password) throws Exception {
        User user = findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid username or password");
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isPasswordValid(String password) {
        return password != null &&
               password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[^A-Za-z0-9].*");
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
} 