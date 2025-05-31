package com.example.demo.service;

import com.example.demo.model.User;
import jakarta.validation.Valid;

public interface UserService {
    User registerUser(User user, String confirmPassword) throws Exception;
    User loginUser(String username, String password) throws Exception;
    User findByUsername(String username);
    boolean isPasswordValid(String password);
    boolean isUsernameExists(String username);
}
