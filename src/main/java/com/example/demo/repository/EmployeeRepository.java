package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = "SELECT emp_id FROM employee ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastEmpId();

    boolean existsByPhone(String phone);

    Employee findByEmpId(String empId);

    @Query("SELECT e FROM Employee e WHERE e.user.username = :email")
    Employee findByEmail(String email);

    java.util.List<Employee> findAllByJoiningDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

    @Query("SELECT e FROM Employee e LEFT JOIN e.projects p WHERE p IS NULL")
    java.util.List<Employee> findBenchEmployees();

    java.util.List<Employee> findAllByOrderByJoiningDateDesc();
} 