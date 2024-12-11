import config.Configuration;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ScheduledExecutorService vendorScheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService customerScheduler = Executors.newSingleThreadScheduledExecutor();

    private static int vendorIndex = 1; // To alternate vendors
    private static int customerIndex = 1; // To alternate customers

    public static void main(String[] args) {
        Configuration config;

        System.out.println("Welcome to the Real-Time Event Ticketing System.");
        System.out.println("1. Load existing configuration");
        System.out.println("2. Create new configuration");
        System.out.print("Enter your choice: ");

        int choice = new Scanner(System.in).nextInt();
        try {
            if (choice == 1) {
                config = Configuration.loadFromFile();
            } else if (choice == 2) {
                config = Configuration.createNewConfiguration();
                config.saveToFile();
            } else {
                System.out.println("Invalid choice. Exiting.");
                return;
            }

            // Initialize ticket pool
            TicketPool ticketPool = new TicketPool(config.getMaxPoolSize(), config.getTotalTicketCount());

            // Schedule vendor and customer tasks
            vendorScheduler.scheduleAtFixedRate(() -> runVendorTask(ticketPool, config),
                    0, config.getVendorReleaseTime(), TimeUnit.MILLISECONDS);

            customerScheduler.scheduleAtFixedRate(() -> runCustomerTask(ticketPool, config),
                    0, config.getCustomerBuyingTime(), TimeUnit.MILLISECONDS);

            // Monitor ticket pool status
            new Thread(() -> monitorPool(ticketPool)).start();

        } catch (IOException e) {
            System.err.println("Error handling configuration: " + e.getMessage());
        }
    }

    private static void runVendorTask(TicketPool pool, Configuration config) {
        synchronized (pool) {
            String vendorName = "Vendor " + vendorIndex;
            if (pool.addTicket()) {
                System.out.println(vendorName + " added a ticket.");
            } else if (pool.getRemainingTickets() == 0) {
                System.out.println("No more tickets to add. Vendors shutting down.");
                vendorScheduler.shutdown();
            }
            vendorIndex = (vendorIndex % 2) + 1; // Alternate vendors
        }
    }

    private static void runCustomerTask(TicketPool pool, Configuration config) {
        synchronized (pool) {
            String customerName = "Customer " + customerIndex;
            if (pool.buyTicket()) {
                System.out.println(customerName + " bought a ticket.");
            }

            if (pool.getCurrentPoolSize() == 0 && pool.getRemainingTickets() == 0) {
                System.out.println("All tickets sold. Customers shutting down.");
                customerScheduler.shutdown();
            }
            customerIndex = (customerIndex % 3) + 1; // Alternate customers
        }
    }

    private static void monitorPool(TicketPool pool) {
        try {
            while (true) {
                synchronized (pool) {
                    int remainingTickets = pool.getRemainingTickets();
                    int currentPoolSize = pool.getCurrentPoolSize();
                    //System.out.println("Monitoring - Pool size: " + currentPoolSize + ", Remaining tickets: " + remainingTickets);

                    if (remainingTickets == 0 && currentPoolSize == 0) {
                        System.out.println("All tickets sold out! Simulation ending.");
                        vendorScheduler.shutdown();
                        customerScheduler.shutdown();
                        return;
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Monitor interrupted: " + e.getMessage());
        }
    }
}
