package com.example.demo.repository;

import com.example.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query(value = "SELECT project_id FROM project ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastProjectId();

    Project findByProjectId(String projectId);
} 