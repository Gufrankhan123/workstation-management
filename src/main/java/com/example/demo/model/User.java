package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role = "ADMIN";
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "otp_failed_attempts")
    private Integer otpFailedAttempts = 0;

    @Column(name = "account_banned")
    private Boolean accountBanned = false;

    @Column(name = "ban_time")
    private java.time.LocalDateTime banTime;
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getOtpFailedAttempts() {
        return otpFailedAttempts == null ? 0 : otpFailedAttempts;
    }

    public void setOtpFailedAttempts(Integer otpFailedAttempts) {
        this.otpFailedAttempts = otpFailedAttempts;
    }

    public boolean isAccountBanned() {
        return accountBanned != null && accountBanned;
    }

    public void setAccountBanned(Boolean accountBanned) {
        this.accountBanned = accountBanned;
    }

    public java.time.LocalDateTime getBanTime() {
        return banTime;
    }

    public void setBanTime(java.time.LocalDateTime banTime) {
        this.banTime = banTime;
    }
} 