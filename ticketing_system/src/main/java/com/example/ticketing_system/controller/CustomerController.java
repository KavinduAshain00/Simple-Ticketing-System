package com.example.ticketing_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ticketing_system.model.Customer;
import com.example.ticketing_system.service.CustomerService;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        System.out.println("Received customer: " + customer);
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId,
            @RequestBody Customer updatedCustomer) {
        try {
            Customer customer = customerService.updateCustomer(customerId, updatedCustomer);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
