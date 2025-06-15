package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import com.example.demo.model.Project;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "relationship_date", nullable = false)
    private LocalDate relationshipDate;

    @ManyToMany(mappedBy = "clients")
    private Set<Project> projects = new java.util.HashSet<>();

    public Client() {}

    public Client(String clientId, String companyName, LocalDate relationshipDate, User user) {
        this.clientId = clientId;
        this.companyName = companyName;
        this.relationshipDate = relationshipDate;
        this.user = user;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getRelationshipDate() { return relationshipDate; }
    public void setRelationshipDate(LocalDate relationshipDate) { this.relationshipDate = relationshipDate; }
    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }
} 