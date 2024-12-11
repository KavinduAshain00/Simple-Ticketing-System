class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerId;

    public int getBuyingTime() {
        return buyingTime;
    }

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
                    // Check if there are any tickets available to buy
                    if (!ticketPool.buyTicket() && ticketPool.getRemainingTickets() > 0) {
                        System.out.println("Customer " + customerId + " could not buy a ticket.");
                        Thread.sleep(buyingTime);  // Wait before trying again
                        continue;  // Skip the rest of the loop and check again
                    }

                    // If a ticket was successfully bought
                    System.out.println("Customer " + customerId + " bought a ticket.");
                }
                Thread.sleep(buyingTime); // Wait after each purchase
            }
        } catch (InterruptedException e) {
            System.err.println("Customer " + customerId + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
