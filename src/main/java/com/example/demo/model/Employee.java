package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "emp_id", unique = true, nullable = false)
    private String empId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "EMPLOYEE";

    public Employee() {}

    public Employee(String empId, String name, String email, LocalDate joiningDate, String password) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.joiningDate = joiningDate;
        this.password = password;
        this.role = "EMPLOYEE";
    }

    public Employee(String empId, String name, String email, LocalDate joiningDate, String password, String role) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.joiningDate = joiningDate;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
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
} 