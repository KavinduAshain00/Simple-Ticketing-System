
import config.Configuration;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    private static ScheduledExecutorService vendorScheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService customerScheduler = Executors.newSingleThreadScheduledExecutor();

    private static int vendorIndex = 1; // To alternate vendors
    private static int customerIndex = 1; // To alternate customers

    public static void main(String[] args) {

        Configuration config;

        // Prompt user to load or create a configuration
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
        } catch (IOException e) {
            System.err.println("Error handling configuration: " + e.getMessage());
            return;
        }

        // Initialize ticket pool
        TicketPool ticketPool = new TicketPool(config.getMaxPoolSize(), config.getTotalTicketCount());
        if (vendorScheduler.isShutdown()) {
            vendorScheduler = Executors.newSingleThreadScheduledExecutor(); // Reinitialize if shutdown
        }
        vendorScheduler.scheduleAtFixedRate(() -> runVendorTask(ticketPool, config),
                0, config.getVendorReleaseTime(), TimeUnit.MILLISECONDS);

        // Step 2: Schedule customers alternately
        if (customerScheduler.isShutdown()) {
            customerScheduler = Executors.newSingleThreadScheduledExecutor(); // Reinitialize if shutdown
        }
        customerScheduler.scheduleAtFixedRate(() -> runCustomerTask(ticketPool, config),
                0, config.getCustomerBuyingTime(), TimeUnit.MILLISECONDS);


//        // Create vendors and customers
//        Thread vendor1 = new Thread(new Vendor(ticketPool, 1, config.getVendorReleaseTime()));
//        Thread vendor2 = new Thread(new Vendor(ticketPool, 2, config.getVendorReleaseTime()));
//        Thread customer1 = new Thread(new Customer(ticketPool, 1, config.getCustomerBuyingTime()));
//        Thread customer2 = new Thread(new Customer(ticketPool, 2, config.getCustomerBuyingTime()));
//        Thread customer3 = new Thread(new Customer(ticketPool, 3, config.getCustomerBuyingTime()));
//
//        // Start simulation
//        System.out.println("Starting the simulation...");
//        vendor1.start();
//        vendor2.start();
//        customer1.start();
//        customer2.start();
//        customer3.start();

        // Monitor ticket status
//        Thread monitorThread = new Thread(() -> {
//            try {
//                while (true) {
//                    int remainingTickets;
//                    int currentPoolSize;
//                    synchronized (ticketPool) {
//                        remainingTickets = ticketPool.getRemainingTickets();
//                        currentPoolSize = ticketPool.getCurrentPoolSize();
//                    }
//                    System.out.println("Real-time status: Pool size = " + currentPoolSize + ", Total tickets left = " + remainingTickets);
//                    if (remainingTickets <= 0 && currentPoolSize == 0) {
//                        System.out.println("All tickets sold out!");
//                        break;
//                    }
//                    Thread.sleep(1000);
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//        monitorThread.start();

        // Wait for threads to complete
//        try {
//            vendor1.join();
//            vendor2.join();
//            customer1.join();
//            customer2.join();
//            customer3.join();
//            monitorThread.join();
//        } catch (InterruptedException e) {
//            System.err.println("Simulation interrupted: " + e.getMessage());
//            Thread.currentThread().interrupt();
//        }



        System.out.println("Simulation completed.");
    }

    private static void runVendorTask(TicketPool pool, Configuration config) {
        synchronized (pool) {
            String vendorName = "Vendor " + vendorIndex;
            if (pool.getRemainingTickets() > 0 && pool.getCurrentPoolSize() < config.getMaxPoolSize()) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() + 1);
                pool.setRemainingTickets(pool.getRemainingTickets() - 1);

                String message = vendorName + " added a ticket. Pool size: " + pool.getCurrentPoolSize() +
                        ", Remaining tickets: " + pool.getRemainingTickets();

            } else if (pool.getRemainingTickets() == 0) {

                vendorScheduler.shutdown();
                return;
            }
            // Alternate vendor index
            vendorIndex = (vendorIndex % 2) + 1; // Alternates between 1 and 2
        }
    }

    private static void runCustomerTask(TicketPool pool, Configuration config) {
        synchronized (pool) {
            String customerName = "Customer " + customerIndex;
            if (pool.getCurrentPoolSize() > 0) {
                pool.setCurrentPoolSize(pool.getCurrentPoolSize() - 1);
                String message = customerName + " bought a ticket. Pool size: " + pool.getCurrentPoolSize();
            }

            // End simulation if no tickets are left
            if (pool.getCurrentPoolSize() == 0 && pool.getRemainingTickets() == 0) {

                customerScheduler.shutdown();
                return;
            }
            // Alternate customer index
            customerIndex = (customerIndex % 3) + 1; // Alternates between 1, 2, and 3
        }
    }
}
