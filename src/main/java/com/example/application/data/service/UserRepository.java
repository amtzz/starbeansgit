package com.example.application.data.service;

import com.example.application.data.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserData, UUID> {
    @Query("SELECT u FROM UserData u WHERE u.username = ?1")
    Optional<UserData> findByUsername(String username);

    @Query("SELECT u FROM UserData u WHERE u.username = ?1 AND u.password =?2")
    Optional<UserData> findByUsername(String username, String password);
}