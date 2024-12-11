class Vendor implements Runnable {
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
                    if (!ticketPool.addTicket()) {
                        break;
                    }
                    System.out.println("Vendor " + vendorId + " added a ticket.");
                }
                Thread.sleep(releaseTime); // Wait after each ticket addition
            }
        } catch (InterruptedException e) {
            System.err.println("Vendor " + vendorId + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
