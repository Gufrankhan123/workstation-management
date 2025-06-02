package com.example.demo.service.impl;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String addEmployee(EmployeeDto dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            return "Email already exists!";
        }
        String empId = generateNextEmpId();
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        Employee emp = new Employee(empId, dto.getName(), dto.getEmail(), dto.getJoiningDate(), hashedPassword, "EMPLOYEE");
        employeeRepository.save(emp);
        return "success";
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
} 