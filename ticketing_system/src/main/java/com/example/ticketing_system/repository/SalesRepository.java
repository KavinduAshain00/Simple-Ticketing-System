package com.example.ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ticketing_system.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
}