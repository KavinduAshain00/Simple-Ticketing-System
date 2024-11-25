package com.example.ticketing_system.service;

import com.example.ticketing_system.model.Sales;
import com.example.ticketing_system.model.Vendor;
import com.example.ticketing_system.model.Ticket;
import com.example.ticketing_system.model.Customer;
import com.example.ticketing_system.repository.SalesRepository;
import com.example.ticketing_system.repository.VendorRepository;
import com.example.ticketing_system.repository.TicketRepository;
import com.example.ticketing_system.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesService {
    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Sales addSale(Long vendorId, Long ticketId, Long customerId) {
        // Validate Vendor
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Validate Ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Ensure the ticket is available
        if (!"AVAILABLE".equalsIgnoreCase(ticket.getStatus())) {
            throw new RuntimeException("Ticket is not available for sale");
        }

        // Validate Customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create and save the sale
        Sales sales = new Sales();
        sales.setVendorId(vendor.getId());
        sales.setTicketId(ticket.getId());
        sales.setCustomerId(customer.getId());

        // Update the ticket status to "SOLD"
        ticket.setStatus("SOLD");
        ticket.setCustomerId(customerId);
        ticketRepository.save(ticket);

        return salesRepository.save(sales);
    }
}
