import java.util.logging.Logger;

public class Vendor implements Runnable {
    private final String name;
    private final TicketPool pool;
    private final int releaseTime;
    private int remainingTickets;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());

    public Vendor(String name, TicketPool pool, int releaseTime, int totalTickets) {
        this.name = name;
        this.pool = pool;
        this.releaseTime = releaseTime;
        this.remainingTickets = totalTickets;
    }

    @Override
    public void run() {
        try {
            while (remainingTickets > 0) {
                Thread.sleep(releaseTime);
                synchronized (pool) {
                    if (pool.addTicket(name + " Ticket #" + remainingTickets)) {
                        logger.info(name + " added a ticket. Remaining tickets: " + --remainingTickets);
                    } else {
                        logger.warning(name + " could not add a ticket. Pool is full.");
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe(name + " interrupted: " + e.getMessage());
        }
    }
}
