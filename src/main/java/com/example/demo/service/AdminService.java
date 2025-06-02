package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.service.dto.EmployeeDto;
import java.util.List;

public interface AdminService {
    List<Employee> getAllEmployees();
    String addEmployee(EmployeeDto employeeDto);
} 