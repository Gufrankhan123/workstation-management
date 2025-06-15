package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.example.demo.model.User;
import java.util.Set;

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

    @Column(name = "dept", nullable = false)
    private String dept;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new java.util.HashSet<>();

    public Employee() {}

    public Employee(String empId, String name, String dept, String phone, LocalDate joiningDate, User user) {
        this.empId = empId;
        this.name = name;
        this.dept = dept;
        this.phone = phone;
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
    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }
} 