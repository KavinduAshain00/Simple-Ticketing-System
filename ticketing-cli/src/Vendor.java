import java.util.logging.Level;
import java.util.logging.Logger;

public class Vendor implements Runnable {
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());

    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releaseTime;

    public Vendor(TicketPool ticketPool, int vendorId, int releaseTime) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.releaseTime = releaseTime;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticketPool) {
                    if (ticketPool.getRemainingTickets() == 0) {
                        logger.info("Vendor " + vendorId + " found no more tickets to add. Stopping.");
                        break;
                    }

                    if (ticketPool.addTicket()) {
                        logger.info("Vendor " + vendorId + " successfully added a ticket.");
                    } else {
                        logger.warning("Vendor " + vendorId + " could not add a ticket (pool full).");
                    }
                }
                Thread.sleep(releaseTime);
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Vendor " + vendorId + " was interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }
}
