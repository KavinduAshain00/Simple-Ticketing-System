import java.util.logging.Logger;

public class Customer implements Runnable {
    private final String name;
    private final TicketPool pool;
    private final int buyTime;
    private static final Logger logger = Logger.getLogger(Customer.class.getName());

    public Customer(String name, TicketPool pool, int buyTime) {
        this.name = name;
        this.pool = pool;
        this.buyTime = buyTime;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(buyTime);
                synchronized (pool) {
                    String ticket = pool.buyTicket();
                    if (ticket != null) {
                        logger.info(name + " bought " + ticket);
                    } else {
                        logger.warning(name + " could not buy a ticket. Pool is empty.");
                        break; // Exit when no tickets are available
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe(name + " interrupted: " + e.getMessage());
        }
    }
}
