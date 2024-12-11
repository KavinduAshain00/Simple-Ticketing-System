import config.Configuration;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer implements Runnable {
    private static final Logger logger = Logger.getLogger(Customer.class.getName()); // Add this line
    private final int id;
    private final TicketPool pool;
    private final Configuration config;
    private final ReentrantLock lock;

    public Customer(int id, TicketPool pool, Configuration config, ReentrantLock lock
                     ) {
        this.id = id;
        this.pool = pool;
        this.config = config;
        this.lock = lock;

    }

    @Override
    public void run() {
        lock.lock();
        try {
            String customerName = "Customer " + id;
            if (pool.getCurrentPoolSize() > 0) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() - 1);
                String message = customerName + " bought a ticket. Pool size: " + pool.getCurrentPoolSize();
                logger.info(message);
            } else {
                // All tickets are sold, break out of the loop to stop the simulation
                logger.info("All tickets are sold. Simulation ending.");

            }
        } finally {
            lock.unlock();
        }
    }
}