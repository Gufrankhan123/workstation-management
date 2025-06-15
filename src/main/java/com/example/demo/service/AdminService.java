package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.service.dto.EmployeeDto;
import com.example.demo.service.dto.ClientDto;
import com.example.demo.service.dto.ProjectDto;
import java.util.List;

public interface AdminService {
    List<Employee> getAllEmployees();
    String addEmployee(EmployeeDto employeeDto);
    List<Client> getAllClients();
    String addClient(ClientDto clientDto);
    List<Project> getAllProjects();
    String addProject(ProjectDto projectDto);
    Project getProjectByProjectId(String projectId);
    String updateProject(String projectId, ProjectDto dto);
    void deleteProject(String projectId);
    Employee getEmployeeByEmpId(String empId);
    Employee getEmployeeByEmail(String email);
    List<Employee> getEmployeesByJoiningDateRange(java.time.LocalDate start, java.time.LocalDate end);
    String updateEmployee(String empId, EmployeeDto dto);
    void deleteEmployee(String empId);
    String assignEmployeeToProject(String empId, String projectId);
    String releaseEmployeeFromProject(String empId);
    List<Employee> getBenchEmployees();
    org.springframework.data.domain.Page<Employee> getEmployeePage(org.springframework.data.domain.Pageable pageable);

    // Client operations
    Client getClientByClientId(String clientId);
    String updateClient(String clientId, ClientDto dto);
    void deleteClient(String clientId);

    // Project helpers
    List<Employee> getEmployeesByProjectId(String projectId);
    Client getClientOfProject(String projectId);
} 