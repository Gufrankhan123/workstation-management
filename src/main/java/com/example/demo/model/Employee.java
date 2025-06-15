package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.example.demo.model.User;

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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    public Employee() {}

    public Employee(String empId, String name, LocalDate joiningDate, User user) {
        this.empId = empId;
        this.name = name;
        this.joiningDate = joiningDate;
        this.user = user;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
} 