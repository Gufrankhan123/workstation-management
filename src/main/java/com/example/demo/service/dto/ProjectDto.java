package com.example.demo.service.dto;

import java.time.LocalDate;
import java.util.Set;

public class ProjectDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private java.util.Set<Integer> clientIds; // ids of Client entities (now supports multiple)
    private Set<Integer> employeeIds; // ids of Employee entities (optional for now)

    public ProjectDto() {}

    // getters setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public java.util.Set<Integer> getClientIds() { return clientIds; }
    public void setClientIds(java.util.Set<Integer> clientIds) { this.clientIds = clientIds; }
    public Set<Integer> getEmployeeIds() { return employeeIds; }
    public void setEmployeeIds(Set<Integer> employeeIds) { this.employeeIds = employeeIds; }
} 