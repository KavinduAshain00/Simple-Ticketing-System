package com.example.ticketing_system.model;

import java.util.concurrent.locks.ReentrantLock;

import com.example.ticketing_system.Repositories.TicketPoolRepository;
import com.example.ticketing_system.controller.RealTimeUpdateController;
import com.example.ticketing_system.service.SimulationService;

public class Customer implements Runnable {
    private final int id;
    private final TicketPool pool;
    private final Configuration config;
    private final ReentrantLock lock;
    private final TicketPoolRepository repository;
    private final RealTimeUpdateController updateController;

    public Customer(int id, TicketPool pool, Configuration config, ReentrantLock lock, TicketPoolRepository repository,
                    RealTimeUpdateController updateController) {
        this.id = id;
        this.pool = pool;
        this.config = config;
        this.lock = lock;
        this.repository = repository;
        this.updateController = updateController;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            String customerName = "Customer " + id;
            if (pool.getCurrentPoolSize() > 0) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() - 1);
                repository.save(pool);
                String message = customerName + " bought a ticket. Pool size: " + pool.getCurrentPoolSize();
                updateController.sendUpdate(message);
            } else {
                // All tickets are sold, break out of the loop to stop the simulation
                updateController.sendUpdate("All tickets are sold. Simulation ending.");
                
            }
        } finally {
            lock.unlock();
        }
    }
}