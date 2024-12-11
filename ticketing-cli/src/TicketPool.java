class TicketPool {
    private final int maxPoolSize;
    private int remainingTickets;
    private int currentPoolSize;

    public TicketPool(int maxPoolSize, int totalTickets) {
        this.maxPoolSize = maxPoolSize;
        this.remainingTickets = totalTickets;
        this.currentPoolSize = 0;
    }

    public synchronized boolean addTicket() {
        if (remainingTickets > 0 && currentPoolSize < maxPoolSize) {
            currentPoolSize++;
            remainingTickets--;
            System.out.println("Ticket added to pool. Pool size: " + currentPoolSize + ", Remaining tickets: " + remainingTickets);
            return true;
        }
        return false;
    }

    public synchronized boolean buyTicket() {
        if (currentPoolSize > 0) {
            currentPoolSize--;
            System.out.println("Ticket bought. Pool size: " + currentPoolSize);
            return true;
        }
        return false;
    }

    public synchronized int getCurrentPoolSize() {
        return currentPoolSize;
    }

    public synchronized int getRemainingTickets() {
        return remainingTickets;
    }
}