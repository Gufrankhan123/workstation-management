package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    long countByAccountBannedTrue();

    java.util.List<User> findByAccountBannedTrue();

    java.util.List<User> findByAccountBannedFalse();

    java.util.List<User> findByAccountBannedTrueAndRoleNot(String role);

    @Query("SELECT u FROM User u WHERE (u.accountBanned IS NULL OR u.accountBanned = false) AND u.role <> 'ADMIN'")
    java.util.List<User> findActiveNonAdmin();
} 