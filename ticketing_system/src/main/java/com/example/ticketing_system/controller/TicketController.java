package com.example.ticketing_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketing_system.model.Ticket;
import com.example.ticketing_system.model.TicketPurchaseRequest;
import com.example.ticketing_system.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/add")
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return ticketService.addTicket(ticket);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@RequestParam Long customerId) {
        Ticket ticket = ticketService.purchaseTicket(customerId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/purchaseWithCustomer")
    public ResponseEntity<String> purchaseTicketWithCustomer(@RequestBody TicketPurchaseRequest request) {
        boolean success = ticketService.purchaseTicketWithCustomer(request);
        if (success) {
            return ResponseEntity.ok("Ticket purchased successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ticket purchase failed. Please try again.");
    }
}
