import config.Configuration;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Vendor implements Runnable {
    private static final Logger logger = Logger.getLogger(Customer.class.getName()); // Add this line

    private final int id;
    private final TicketPool pool;
    private final Configuration config;
    private final ReentrantLock lock;


    public Vendor(int id, TicketPool pool, Configuration config, ReentrantLock lock
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
            String vendorName = "Vendor " + id;
            if (pool.getRemainingTickets() > 0 && pool.getCurrentPoolSize() < config.getMaxPoolSize()) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() + 1);
                pool.setRemainingTickets(pool.getRemainingTickets() - 1);

                String message = vendorName + " added a ticket. Pool size: " + pool.getCurrentPoolSize() +
                        ", Remaining tickets: " + pool.getRemainingTickets();
                logger.info(message);
            } else if (pool.getRemainingTickets() == 0) {
                logger.info(vendorName + " has no more tickets to add. Stopping.");
                return;
            }
        } finally {
            lock.unlock();
        }
    }
}