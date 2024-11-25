package com.example.ticketing_system.controller;

import com.example.ticketing_system.service.SalesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @PostMapping("/add")
    public ResponseEntity<String> addSale(
            @RequestParam Long vendorId,
            @RequestParam Long ticketId,
            @RequestParam Long customerId) {
        salesService.addSale(vendorId, ticketId, customerId);
        return ResponseEntity.ok("Successfully purchased a ticket!");
    }
}
