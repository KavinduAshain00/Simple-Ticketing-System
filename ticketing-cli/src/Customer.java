class Customer implements Runnable {
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
                if (!ticketPool.buyTicket() && ticketPool.getRemainingTickets() <= 0) {
                    break;
                }
                System.out.println("Customer " + customerId + " bought a ticket.");
                Thread.sleep(buyingTime);
            }
        } catch (InterruptedException e) {
            System.err.println("Customer " + customerId + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}