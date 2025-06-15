package com.example.demo.service.dto;

import java.time.LocalDate;

public class ClientDto {
    private String companyName;
    private String email;
    private LocalDate relationshipDate;
    private String password;

    public ClientDto() {}

    // Getters and setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getRelationshipDate() { return relationshipDate; }
    public void setRelationshipDate(LocalDate relationshipDate) { this.relationshipDate = relationshipDate; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 