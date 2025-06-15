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
} 