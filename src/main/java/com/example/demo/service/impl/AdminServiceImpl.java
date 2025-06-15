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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        return employeeRepository.findAllByOrderByJoiningDateDesc();
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public String addEmployee(EmployeeDto dto) {
        // Validate uniqueness of email & phone
        if (userRepository.findByUsername(dto.getEmail()) != null) {
            return "Email already exists!";
        }
        if (employeeRepository.existsByPhone(dto.getPhone())) {
            return "Phone already exists!";
        }

        // 1) Create credential record
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("EMPLOYEE");
        userRepository.save(user);

        // 2) Create employee profile record
        String empId = generateNextEmpId();
        Employee emp = new Employee(empId, dto.getName(), dto.getDept(), dto.getPhone(), dto.getJoiningDate(), user);
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
        // find clients (must have at least one)
        java.util.Set<Client> clis = dto.getClientIds() != null ? new java.util.HashSet<>(clientRepository.findAllById(dto.getClientIds())) : java.util.Set.of();
        if (clis.isEmpty()) {
            return "Invalid client(s)";
        }

        String projectId = generateNextProjectId();
        Project project = new Project(projectId, dto.getName(), dto.getStartDate(), dto.getEndDate());
        project.getClients().addAll(clis);

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

        if (dto.getClientIds() != null) {
            existing.getClients().clear();
            existing.getClients().addAll(clientRepository.findAllById(dto.getClientIds()));
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

    @Override
    public Employee getEmployeeByEmpId(String empId) {
        return employeeRepository.findByEmpId(empId);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<Employee> getEmployeesByJoiningDateRange(LocalDate start, LocalDate end) {
        return employeeRepository.findAllByJoiningDateBetween(start, end);
    }

    @Override
    public String updateEmployee(String empId, EmployeeDto dto) {
        Employee existing = employeeRepository.findByEmpId(empId);
        if (existing == null) return "Employee not found";
        existing.setName(dto.getName());
        existing.setDept(dto.getDept());
        existing.setPhone(dto.getPhone());
        existing.setJoiningDate(dto.getJoiningDate());
        employeeRepository.save(existing);
        return "success";
    }

    @Override
    public void deleteEmployee(String empId) {
        Employee existing = employeeRepository.findByEmpId(empId);
        if (existing != null) employeeRepository.delete(existing);
    }

    @Override
    public String assignEmployeeToProject(String empId, String projectId) {
        Employee emp = employeeRepository.findByEmpId(empId);
        Project project = projectRepository.findByProjectId(projectId);
        if (emp == null || project == null) return "Invalid employee or project";
        if (!emp.getProjects().isEmpty()) return "Employee already assigned";
        emp.getProjects().add(project);
        project.getEmployees().add(emp);
        employeeRepository.save(emp);
        projectRepository.save(project);
        return "success";
    }

    @Override
    public String releaseEmployeeFromProject(String empId) {
        Employee emp = employeeRepository.findByEmpId(empId);
        if (emp == null) return "Employee not found";
        if (emp.getProjects().isEmpty()) return "Employee not assigned";
        for (Project pr : emp.getProjects()) {
            pr.getEmployees().remove(emp);
        }
        emp.getProjects().clear();
        employeeRepository.save(emp);
        return "success";
    }

    @Override
    public List<Employee> getBenchEmployees() {
        return employeeRepository.findBenchEmployees();
    }

    @Override
    public Page<Employee> getEmployeePage(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Client getClientByClientId(String clientId) {
        return clientRepository.findByClientId(clientId);
    }

    @Override
    public String updateClient(String clientId, ClientDto dto) {
        Client existing = clientRepository.findByClientId(clientId);
        if (existing == null) return "Client not found";
        existing.setCompanyName(dto.getCompanyName());
        existing.setRelationshipDate(dto.getRelationshipDate());

        User user = existing.getUser();
        // Update email if provided and unique
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && !dto.getEmail().equalsIgnoreCase(user.getUsername())) {
            // Reject duplicate email
            if (userRepository.findByUsername(dto.getEmail()) != null) {
                return "Email already exists!";
            }
            user.setUsername(dto.getEmail());
        }

        // Update password if provided (blank means keep existing)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        clientRepository.save(existing);
        return "success";
    }

    @Override
    public void deleteClient(String clientId) {
        Client existing = clientRepository.findByClientId(clientId);
        if (existing != null) clientRepository.delete(existing);
    }

    @Override
    public List<Employee> getEmployeesByProjectId(String projectId) {
        Project project = projectRepository.findByProjectId(projectId);
        return project != null ? new java.util.ArrayList<>(project.getEmployees()) : java.util.List.of();
    }

    @Override
    public Client getClientOfProject(String projectId) {
        Project project = projectRepository.findByProjectId(projectId);
        if (project != null && !project.getClients().isEmpty()) {
            return project.getClients().iterator().next();
        }
        return null;
    }

    private String generateNextEmpId() {
        String lastEmpId = employeeRepository.findLastEmpId();
        int nextId = 1;
        if (lastEmpId != null && lastEmpId.startsWith("JTC-")) {
            try {
                nextId = Integer.parseInt(lastEmpId.substring(4)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("JTC-%03d", nextId);
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