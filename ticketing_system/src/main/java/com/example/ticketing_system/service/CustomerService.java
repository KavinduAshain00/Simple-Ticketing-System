package com.example.ticketing_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.model.Customer;
import com.example.ticketing_system.repository.CustomerRepository;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        return customer;
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email); // Ensure the repository has this method
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        return customerRepository.findById(customerId)
            .map(customer -> {
                customer.setName(updatedCustomer.getName());
                customer.setVip(updatedCustomer.getVip());
                return customerRepository.save(customer);
            })
            .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }

}
