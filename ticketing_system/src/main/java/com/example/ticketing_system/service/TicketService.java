package com.example.ticketing_system.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.model.Customer;
import com.example.ticketing_system.model.Ticket;
import com.example.ticketing_system.model.TicketPurchaseRequest;
import com.example.ticketing_system.repository.TicketRepository;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    private CustomerService customerService;

    private final Object lock = new Object();

    public Ticket addTicket(Ticket ticket) {
        synchronized (lock) {
            ticket.setStatus("AVAILABLE");
            return ticketRepository.save(ticket);
        }
    }

    public Ticket purchaseTicket(Long customerId) {
        synchronized (lock) {
            Optional<Customer> customer = customerService.getCustomerById(customerId);
            if (customer.isPresent()) {
                Optional<Ticket> availableTicket = ticketRepository.findByStatus("AVAILABLE").stream().findFirst();
                if (availableTicket.isPresent()) {
                    Ticket ticket = availableTicket.get();
                    ticket.setStatus("SOLD");
                    ticket.setCustomerId(customerId);
                    return ticketRepository.save(ticket);
                }
            }
            return null; // No tickets available or customer not found
        }
    }
    public boolean purchaseTicketWithCustomer(TicketPurchaseRequest request) {
        // Example logic: Validate and process the ticket purchase
        if (request.getCustomerId() != null && request.getQuantity() > 0 && request.getQuantity() <= 10) {
            System.out.println("Customer ID: " + request.getCustomerId());
            System.out.println("Ticket purchased: " + request.getTicketType() +
                               ", Quantity: " + request.getQuantity() +
                               ", Time: " + request.getTime());
            // Save to database or process payment
            return true;
        }
        return false; // Indicate failure
    }
}

