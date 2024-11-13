package com.example.ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketing_system.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Additional query methods if needed
}