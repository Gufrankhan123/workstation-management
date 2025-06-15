package com.example.demo.service;

import com.example.demo.model.User;

public interface OtpService {
    void generateOtp(User user);
    boolean validateOtp(User user, String code) throws Exception;
    void resendOtp(User user);
} 