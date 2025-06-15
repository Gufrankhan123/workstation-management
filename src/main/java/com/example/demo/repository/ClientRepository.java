package com.example.demo.repository;

import com.example.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query(value = "SELECT client_id FROM client ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastClientId();

    Client findByClientId(String clientId);
} 