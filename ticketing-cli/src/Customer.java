import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer implements Runnable {
    private static final Logger logger = Logger.getLogger(Customer.class.getName());
    private final TicketPool ticketPool;
    private final int customerId;
    private final int buyingTime;

    public Customer(TicketPool ticketPool, int customerId, int buyingTime) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.buyingTime = buyingTime;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticketPool) {
                    if (ticketPool.getRemainingTickets() == 0 && ticketPool.getCurrentPoolSize() == 0) {
                        logger.info("Customer " + customerId + " found no tickets left. Stopping.");
                        break;
                    }

                    if (!ticketPool.buyTicket()) {
                        logger.warning("Customer " + customerId + " could not buy a ticket. Retrying...");
                    } else {
                        logger.info("Customer " + customerId + " successfully bought a ticket.");
                    }
                }
                Thread.sleep(buyingTime);
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Customer " + customerId + " was interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }
}