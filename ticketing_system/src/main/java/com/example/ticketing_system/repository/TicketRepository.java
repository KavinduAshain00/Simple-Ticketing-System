package com.example.ticketing_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticketing_system.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(String status);
}
