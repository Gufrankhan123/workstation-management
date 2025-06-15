package com.example.demo.service.dto;

import java.time.LocalDate;

public class EmployeeDto {
    private String name;
    private String dept;
    private String phone;
    private String email;
    private LocalDate joiningDate;
    private String password;
    private String role = "EMPLOYEE";

    public EmployeeDto() {}

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
} 