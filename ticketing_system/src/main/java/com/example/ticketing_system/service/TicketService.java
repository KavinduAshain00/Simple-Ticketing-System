package com.example.ticketing_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.model.Ticket;
import com.example.ticketing_system.repository.TicketRepository;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    private final Object lock = new Object();

    public Ticket addTicket(Ticket ticket) {
        synchronized (lock) {
            ticket.setStatus("AVAILABLE");
            return ticketRepository.save(ticket);
        }
    }
}

