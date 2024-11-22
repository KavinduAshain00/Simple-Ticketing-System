package com.example.ticketing_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.model.Vendor;
import com.example.ticketing_system.repository.VendorRepository;

import java.util.Optional;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Optional<Vendor> getVendorById(Long vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);
        if(vendor.isEmpty()){
            throw new RuntimeException("Vendor not found with ID: " + vendorId);
        }
        return vendor;
    }

    public Optional<Vendor> getVendorByEmail(String email) {
        return vendorRepository.findByEmail(email); // Ensure the repository has this method
    }

    public void deleteVendor(Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    // Other methods as needed
}
