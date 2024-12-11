import java.util.logging.Level;
import java.util.logging.Logger;

class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    private final int maxPoolSize;
    private int remainingTickets;
    private int currentPoolSize;

    public TicketPool(int maxPoolSize, int totalTickets) {
        this.maxPoolSize = maxPoolSize;
        this.remainingTickets = totalTickets;
        this.currentPoolSize = 0;
        logger.info("TicketPool initialized with maxPoolSize=" + maxPoolSize + ", totalTickets=" + totalTickets);
    }

    public synchronized boolean addTicket() {
        if (remainingTickets > 0 && currentPoolSize < maxPoolSize) {
            currentPoolSize++;
            remainingTickets--;
            logger.info("Ticket added to pool. Pool size: " + currentPoolSize + ", Remaining tickets: " + remainingTickets);
            return true;
        }
        logger.warning("Unable to add ticket. Pool size: " + currentPoolSize + ", Remaining tickets: " + remainingTickets);
        return false;
    }

    public synchronized boolean buyTicket() {
        if (currentPoolSize > 0) {
            currentPoolSize--;
            logger.info("Ticket bought. Pool size: " + currentPoolSize);
            return true;
        }
        logger.warning("Unable to buy ticket. Pool size: " + currentPoolSize);
        return false;
    }

    public synchronized int getCurrentPoolSize() {
        return currentPoolSize;
    }

    public synchronized int getRemainingTickets() {
        return remainingTickets;
    }
}