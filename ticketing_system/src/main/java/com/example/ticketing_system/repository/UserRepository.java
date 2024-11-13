package com.example.ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticketing_system.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
