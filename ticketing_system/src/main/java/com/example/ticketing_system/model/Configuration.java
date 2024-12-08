package com.example.ticketing_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalTickets;
    public int getTotalTickets() {
        return totalTickets;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    private int maxPoolSize;
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
    private int vendorReleaseTime; // in milliseconds
    public int getVendorReleaseTime() {
        return vendorReleaseTime;
    }
    public void setVendorReleaseTime(int vendorReleaseTime) {
        this.vendorReleaseTime = vendorReleaseTime;
    }
    private int customerBuyTime;  // in milliseconds
    public int getCustomerBuyTime() {
        return customerBuyTime;
    }
    public void setCustomerBuyTime(int customerBuyTime) {
        this.customerBuyTime = customerBuyTime;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}

