package com.example.ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketing_system.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    // Additional query methods if needed
}
