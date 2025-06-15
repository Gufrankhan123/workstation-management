-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS workstation_management;

-- Use the database
USE workstation_management;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'owner',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_username UNIQUE (username)
); 

-- Create employee table
CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    joining_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_emp_id UNIQUE (emp_id),
    CONSTRAINT fk_employee_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create client table
CREATE TABLE IF NOT EXISTS client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(50) NOT NULL UNIQUE,
    company_name VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    relationship_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_client_id UNIQUE (client_id),
    CONSTRAINT fk_client_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create project table
CREATE TABLE IF NOT EXISTS project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    client_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_project_id UNIQUE (project_id),
    CONSTRAINT fk_project_client FOREIGN KEY (client_id) REFERENCES client(id)
);

-- Create join table for project and employee
CREATE TABLE IF NOT EXISTS project_employee (
    project_id INT NOT NULL,
    employee_id INT NOT NULL,
    PRIMARY KEY (project_id, employee_id),
    CONSTRAINT fk_pe_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT fk_pe_employee FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

