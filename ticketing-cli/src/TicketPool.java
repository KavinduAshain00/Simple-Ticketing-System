import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class TicketPool {
    private final LinkedBlockingQueue<String> tickets;
    private final int maxSize;
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    public TicketPool(int maxSize) {
        this.maxSize = maxSize;
        this.tickets = new LinkedBlockingQueue<>(maxSize);
    }

    public synchronized boolean addTicket(String ticket) {
        if (tickets.size() < maxSize) {
            tickets.add(ticket);
            logger.info("Ticket added: " + ticket + " | Available tickets: " + tickets.size());
            notifyAll(); // Notify customers that a ticket is available
            return true;
        }
        logger.warning("Failed to add ticket. Pool is full.");
        return false;
    }

    public synchronized String buyTicket() {
        try {
            while (tickets.isEmpty()) {
                logger.info("Waiting for tickets...");
                wait(); // Wait until a ticket is available
            }
            String ticket = tickets.poll();
            logger.info("Ticket bought: " + ticket + " | Remaining tickets: " + tickets.size());
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Thread interrupted while buying a ticket: " + e.getMessage());
            return null;
        }
    }

    public int getAvailableTickets() {
        return tickets.size();
    }
}
