package com.example.ticketing_system.model;

import java.util.concurrent.locks.ReentrantLock;

import com.example.ticketing_system.Repositories.TicketPoolRepository;
import com.example.ticketing_system.controller.RealTimeUpdateController;

public class Vendor implements Runnable {
    private final int id;
    private final TicketPool pool;
    private final Configuration config;
    private final ReentrantLock lock;
    private final TicketPoolRepository repository;
    private final RealTimeUpdateController updateController;

    public Vendor(int id, TicketPool pool, Configuration config, ReentrantLock lock, TicketPoolRepository repository,
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
            String vendorName = "Vendor " + id;
            if (pool.getRemainingTickets() > 0 && pool.getCurrentPoolSize() < config.getMaxPoolSize()) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() + 1);
                pool.setRemainingTickets(pool.getRemainingTickets() - 1);
                repository.save(pool);
                String message = vendorName + " added a ticket. Pool size: " + pool.getCurrentPoolSize() +
                        ", Remaining tickets: " + pool.getRemainingTickets();
                updateController.sendUpdate(message);
            } else if (pool.getRemainingTickets() == 0) {
                updateController.sendUpdate(vendorName + " has no more tickets to add. Stopping.");
                return;
            }
        } finally {
            lock.unlock();
        }
    }
}