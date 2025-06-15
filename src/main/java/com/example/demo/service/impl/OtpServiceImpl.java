package com.example.demo.service.impl;

import com.example.demo.model.OtpToken;
import com.example.demo.model.User;
import com.example.demo.repository.OtpTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpServiceImpl implements OtpService {

    private static final int OTP_VALIDITY_SECONDS = 30;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void generateOtp(User user) {
        // Invalidate existing tokens
        otpTokenRepository.findTopByUserAndUsedFalseOrderByIssuedAtDesc(user)
                .ifPresent(token -> {
                    token.setUsed(true);
                    otpTokenRepository.save(token);
                });

        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        OtpToken token = new OtpToken(user, encoder.encode(code), Instant.now(), Instant.now().plusSeconds(OTP_VALIDITY_SECONDS));
        otpTokenRepository.save(token);
        emailService.sendOtpEmail(user.getUsername(), code); // username is email in this system
    }

    @Override
    public boolean validateOtp(User user, String code) throws Exception {
        if (user.isAccountBanned()) {
            throw new Exception("Your account is banned due to suspicious activity. Please contact admin.");
        }

        OtpToken token = otpTokenRepository.findTopByUserAndUsedFalseOrderByIssuedAtDesc(user)
                .orElseThrow(() -> new Exception("OTP expired or not found"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            token.setUsed(true);
            otpTokenRepository.save(token);
            throw new Exception("OTP expired");
        }

        boolean matches = encoder.matches(code, token.getCodeHash());
        if (matches) {
            token.setUsed(true);
            otpTokenRepository.save(token);
            user.setOtpFailedAttempts(0);
            userRepository.save(user);
            return true;
        } else {
            int attempts = user.getOtpFailedAttempts() + 1;
            user.setOtpFailedAttempts(attempts);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountBanned(true);
                user.setBanTime(java.time.LocalDateTime.now());
            }
            userRepository.save(user);
            throw new Exception(attempts >= MAX_FAILED_ATTEMPTS ? "Account banned due to too many invalid attempts" : "Invalid OTP");
        }
    }

    @Override
    public void resendOtp(User user) {
        if (user.isAccountBanned()) {
            return; // do nothing
        }
        generateOtp(user);
    }
} 