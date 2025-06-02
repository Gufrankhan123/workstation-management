package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);

    @Query(value = "SELECT emp_id FROM employee ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastEmpId();
} 