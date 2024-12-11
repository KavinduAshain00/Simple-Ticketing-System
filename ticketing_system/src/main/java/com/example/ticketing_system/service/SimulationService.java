package com.example.ticketing_system.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.Repositories.ConfigurationRepository;
import com.example.ticketing_system.Repositories.TicketPoolRepository;
import com.example.ticketing_system.controller.RealTimeUpdateController;
import com.example.ticketing_system.model.Configuration;
import com.example.ticketing_system.model.TicketPool;
import com.example.ticketing_system.model.Vendor;
import com.example.ticketing_system.model.Customer;

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

    private final ReentrantLock lock = new ReentrantLock(); // To ensure thread safety


    public void startSimulation(Configuration config) {

        // Reset the ticket pool
        ticketPoolRepository.deleteAll();
        TicketPool pool = new TicketPool(0, config.getTotalTickets());

        // Reinitialize schedulers if they are shutdown
        if (vendorScheduler.isShutdown()) {
            vendorScheduler = Executors.newSingleThreadScheduledExecutor();
        }
        if (customerScheduler.isShutdown()) {
            customerScheduler = Executors.newSingleThreadScheduledExecutor();
        }

        // Vendor logic
        vendorScheduler.execute(() -> {
            int vendorId = 1;
            while (true) { // Check stop flag
                lock.lock();
                try {
                    if (pool.getRemainingTickets() > 0) {
                        new Vendor(vendorId, pool, config, lock, ticketPoolRepository, updateController).run();
                        vendorId = (vendorId % 2) + 1; // Switch between Vendor 1 and Vendor 2
                    } else {
                        updateController.sendUpdate("Vendor " + vendorId + " has no more tickets to add. Stopping.");
                        break;
                    }
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(config.getVendorReleaseTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Interrupt to stop the thread
                    break;
                }
            }
        });

        // Customer logic
        customerScheduler.execute(() -> {
            int customerId = 1;
            while (true) { // Check stop flag
                lock.lock();
                try {
                    if (pool.getCurrentPoolSize() > 0 || pool.getRemainingTickets() > 0) {
                        new Customer(customerId, pool, config, lock, ticketPoolRepository, updateController).run();
                        customerId = (customerId % 3) + 1; // Switch between Customer 1, 2, and 3
                    } else {
                        updateController.sendUpdate("All tickets are sold. Simulation ending.");
                        break;
                    }
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(config.getCustomerBuyTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Interrupt to stop the thread
                    break;
                }
            }
        });
    }

    public synchronized void stopSimulation() {
   

        // Shutdown the schedulers gracefully
        if (!vendorScheduler.isShutdown()) {
            vendorScheduler.shutdownNow();
        }
        if (!customerScheduler.isShutdown()) {
            customerScheduler.shutdownNow();
        }

        // Log simulation stopped only if it was running
        updateController.sendUpdate("Simulation stopped.");
    }
}

