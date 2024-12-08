package com.example.ticketing_system.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.Repositories.TicketPoolRepository;
import com.example.ticketing_system.controller.RealTimeUpdateController;
import com.example.ticketing_system.model.Configuration;
import com.example.ticketing_system.model.TicketPool;

@Service
public class SimulationService {

    @Autowired
    private TicketPoolRepository ticketPoolRepository;

    @Autowired
    private RealTimeUpdateController updateController;

    private final ScheduledExecutorService vendorScheduler = Executors.newScheduledThreadPool(2);
    private final ScheduledExecutorService customerScheduler = Executors.newScheduledThreadPool(3);

    public void startSimulation(Configuration config) {
        ticketPoolRepository.deleteAll();
        TicketPool pool = new TicketPool(0, config.getTotalTickets());
        ticketPoolRepository.save(pool);

        // Vendor task: Adds tickets to the pool
        Runnable vendorTask = () -> {
            synchronized (pool) {
                if (pool.getRemainingTickets() > 0 && pool.getCurrentPoolSize() < config.getMaxPoolSize()) {
                    pool.setCurrentPoolSize(pool.getCurrentPoolSize() + 1);
                    pool.setRemainingTickets(pool.getRemainingTickets() - 1);
                    ticketPoolRepository.save(pool);
                    String message = "Vendor added a ticket. Pool size: " + pool.getCurrentPoolSize() +
                                     ", Remaining tickets: " + pool.getRemainingTickets();
                    updateController.sendUpdate(message);
                } else if (pool.getRemainingTickets() == 0) {
                    updateController.sendUpdate("No more tickets to add. Vendors are stopping.");
                    vendorScheduler.shutdown();
                }
            }
        };

        // Customer task: Buys a ticket from the pool
        Runnable customerTask = () -> {
            synchronized (pool) {
                if (pool.getCurrentPoolSize() > 0) {
                    pool.setCurrentPoolSize(pool.getCurrentPoolSize() - 1);
                    ticketPoolRepository.save(pool);
                    String message = "Customer bought a ticket. Pool size: " + pool.getCurrentPoolSize();
                    updateController.sendUpdate(message);
                }

                // End the simulation when the pool and remaining tickets are both empty
                if (pool.getCurrentPoolSize() == 0 && pool.getRemainingTickets() == 0) {
                    updateController.sendUpdate("All tickets are sold. Simulation ending.");
                    customerScheduler.shutdown(); // Stop the entire simulation
                }
            }
        };

        // Step 1: Schedule vendors
        for (int i = 0; i < 2; i++) {
            vendorScheduler.scheduleAtFixedRate(vendorTask, i * config.getVendorReleaseTime(), config.getVendorReleaseTime(), TimeUnit.MILLISECONDS);
        }

        // Step 2: Schedule customers
        for (int i = 0; i < 3; i++) {
            customerScheduler.scheduleAtFixedRate(customerTask, i * config.getCustomerBuyTime(), config.getCustomerBuyTime(), TimeUnit.MILLISECONDS);
        }
    }
}
