package com.example.ticketing_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TicketPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentPoolSize;

    public TicketPool() {
    }

    // Constructor with parameters
    public TicketPool(int currentPoolSize, int remainingTickets) {
        this.currentPoolSize = currentPoolSize;
        this.remainingTickets = remainingTickets;
    }

    public int getCurrentPoolSize() {
        return currentPoolSize;
    }
    public void setCurrentPoolSize(int currentPoolSize) {
        this.currentPoolSize = currentPoolSize;
    }
    private int remainingTickets;
    public int getRemainingTickets() {
        return remainingTickets;
    }
    public void setRemainingTickets(int remainingTickets) {
        this.remainingTickets = remainingTickets;
    }
}

