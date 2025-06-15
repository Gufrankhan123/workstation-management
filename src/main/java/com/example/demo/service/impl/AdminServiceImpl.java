package com.example.demo.service.impl;

import com.example.demo.model.Employee;
import com.example.demo.model.User;
import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.dto.EmployeeDto;
import com.example.demo.service.dto.ClientDto;
import com.example.demo.service.dto.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public String addEmployee(EmployeeDto dto) {
        // Check if user/email already exists in users table
        if (userRepository.findByUsername(dto.getEmail()) != null) {
            return "Email already exists!";
        }

        // 1) Create credential record
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("EMPLOYEE");
        userRepository.save(user);

        // 2) Create employee profile record
        String empId = generateNextEmpId();
        Employee emp = new Employee(empId, dto.getName(), dto.getJoiningDate(), user);
        employeeRepository.save(emp);

        return "success";
    }

    @Override
    public String addClient(ClientDto dto) {
        // Ensure email unique in users
        if (userRepository.findByUsername(dto.getEmail()) != null) {
            return "Email already exists!";
        }

        // Create user credentials
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("CLIENT");
        userRepository.save(user);

        // Create client profile
        String clientId = generateNextClientId();
        Client client = new Client(clientId, dto.getCompanyName(), dto.getRelationshipDate(), user);
        clientRepository.save(client);

        return "success";
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public String addProject(ProjectDto dto) {
        // find client
        Client client = clientRepository.findById(dto.getClientId()).orElse(null);
        if (client == null) {
            return "Invalid client";
        }

        String projectId = generateNextProjectId();
        Project project = new Project(projectId, dto.getName(), dto.getStartDate(), dto.getEndDate(), client);

        // add employees if provided
        if (dto.getEmployeeIds() != null && !dto.getEmployeeIds().isEmpty()) {
            List<Employee> emps = employeeRepository.findAllById(dto.getEmployeeIds());
            project.getEmployees().addAll(emps);
        }

        projectRepository.save(project);
        return "success";
    }

    @Override
    public Project getProjectByProjectId(String projectId) {
        return projectRepository.findByProjectId(projectId);
    }

    @Override
    public String updateProject(String projectId, ProjectDto dto) {
        Project existing = projectRepository.findByProjectId(projectId);
        if (existing == null) return "Project not found";

        existing.setName(dto.getName());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());

        if (dto.getClientId() != null) {
            clientRepository.findById(dto.getClientId()).ifPresent(existing::setClient);
        }

        if (dto.getEmployeeIds() != null) {
            existing.getEmployees().clear();
            existing.getEmployees().addAll(employeeRepository.findAllById(dto.getEmployeeIds()));
        }

        projectRepository.save(existing);
        return "success";
    }

    @Override
    public void deleteProject(String projectId) {
        Project existing = projectRepository.findByProjectId(projectId);
        if (existing != null) projectRepository.delete(existing);
    }

    private String generateNextEmpId() {
        String lastEmpId = employeeRepository.findLastEmpId();
        int nextId = 1;
        if (lastEmpId != null && lastEmpId.startsWith("EMP")) {
            try {
                nextId = Integer.parseInt(lastEmpId.substring(3)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("EMP%03d", nextId);
    }

    private String generateNextClientId() {
        String lastClientId = clientRepository.findLastClientId();
        int nextId = 1;
        if (lastClientId != null && lastClientId.startsWith("CLIENT")) {
            try {
                nextId = Integer.parseInt(lastClientId.substring(6)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("CLIENT%03d", nextId);
    }

    private String generateNextProjectId() {
        String lastId = projectRepository.findLastProjectId();
        int next = 1;
        if (lastId != null && lastId.startsWith("PROJECT")) {
            try { next = Integer.parseInt(lastId.substring(7)) + 1;} catch(Exception ignored) {}
        }
        return String.format("PROJECT%03d", next);
    }
} 