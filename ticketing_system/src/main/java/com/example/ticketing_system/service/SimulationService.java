package com.example.ticketing_system.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.Repositories.ConfigurationRepository;
import com.example.ticketing_system.Repositories.TicketPoolRepository;
import com.example.ticketing_system.controller.RealTimeUpdateController;
import com.example.ticketing_system.model.Configuration;
import com.example.ticketing_system.model.TicketPool;

@Service
public class SimulationService {

    @Autowired
    private TicketPoolRepository ticketPoolRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private RealTimeUpdateController updateController;

    private ScheduledExecutorService vendorScheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService customerScheduler = Executors.newSingleThreadScheduledExecutor();

    private int vendorIndex = 1; // To alternate vendors
    private int customerIndex = 1; // To alternate customers

    public void startSimulation(Configuration config) {
        // Reset the ticket pool
        ticketPoolRepository.deleteAll();
        TicketPool pool = new TicketPool(0, config.getTotalTickets());
        ticketPoolRepository.save(pool);

        // Step 1: Schedule vendors alternately
        if (vendorScheduler.isShutdown()) {
            vendorScheduler = Executors.newSingleThreadScheduledExecutor(); // Reinitialize if shutdown
        }
        vendorScheduler.scheduleAtFixedRate(() -> runVendorTask(pool, config), 
            0, config.getVendorReleaseTime(), TimeUnit.MILLISECONDS);

        // Step 2: Schedule customers alternately
        if (customerScheduler.isShutdown()) {
            customerScheduler = Executors.newSingleThreadScheduledExecutor(); // Reinitialize if shutdown
        }
        customerScheduler.scheduleAtFixedRate(() -> runCustomerTask(pool, config), 
            0, config.getCustomerBuyTime(), TimeUnit.MILLISECONDS);
    }

    public void stopSimulation() {
        // Shutdown the schedulers gracefully
        vendorScheduler.shutdown();
        customerScheduler.shutdown();
        updateController.sendUpdate("Simulation stopped.");
    }

    private void runVendorTask(TicketPool pool, Configuration config) {
        synchronized (pool) {
            String vendorName = "Vendor " + vendorIndex;
            if (pool.getRemainingTickets() > 0 && pool.getCurrentPoolSize() < config.getMaxPoolSize()) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() + 1);
                pool.setRemainingTickets(pool.getRemainingTickets() - 1);
                ticketPoolRepository.save(pool);
                String message = vendorName + " added a ticket. Pool size: " + pool.getCurrentPoolSize() +
                        ", Remaining tickets: " + pool.getRemainingTickets();
                updateController.sendUpdate(message);
            } else if (pool.getRemainingTickets() == 0) {
                updateController.sendUpdate(vendorName + " has no more tickets to add. Stopping.");
                vendorScheduler.shutdown();
                return;
            }
            // Alternate vendor index
            vendorIndex = (vendorIndex % 2) + 1; // Alternates between 1 and 2
        }
    }

    private void runCustomerTask(TicketPool pool, Configuration config) {
        synchronized (pool) {
            String customerName = "Customer " + customerIndex;
            if (pool.getCurrentPoolSize() > 0) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() - 1);
                ticketPoolRepository.save(pool);
                String message = customerName + " bought a ticket. Pool size: " + pool.getCurrentPoolSize();
                updateController.sendUpdate(message);
            }

            // End simulation if no tickets are left
            if (pool.getCurrentPoolSize() == 0 && pool.getRemainingTickets() == 0) {
                updateController.sendUpdate("All tickets are sold. Simulation ending.");
                customerScheduler.shutdown();
                return;
            }
            // Alternate customer index
            customerIndex = (customerIndex % 3) + 1; // Alternates between 1, 2, and 3
        }
    }
}
