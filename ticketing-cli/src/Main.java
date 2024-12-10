
import config.Configuration;
import java.io.IOException;
import java.util.Scanner;

public class Main {
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

        // Create vendors and customers
        Thread vendor1 = new Thread(new Vendor(ticketPool, 1, config.getVendorReleaseTime()));
        Thread vendor2 = new Thread(new Vendor(ticketPool, 2, config.getVendorReleaseTime()));
        Thread customer1 = new Thread(new Customer(ticketPool, 1, config.getCustomerBuyingTime()));
        Thread customer2 = new Thread(new Customer(ticketPool, 2, config.getCustomerBuyingTime()));
        Thread customer3 = new Thread(new Customer(ticketPool, 3, config.getCustomerBuyingTime()));

        // Start simulation
        System.out.println("Starting the simulation...");
        vendor1.start();
        vendor2.start();
        customer1.start();
        customer2.start();
        customer3.start();

        // Monitor ticket status
        Thread monitorThread = new Thread(() -> {
            try {
                while (true) {
                    int remainingTickets;
                    int currentPoolSize;
                    synchronized (ticketPool) {
                        remainingTickets = ticketPool.getRemainingTickets();
                        currentPoolSize = ticketPool.getCurrentPoolSize();
                    }
                    System.out.println("Real-time status: Pool size = " + currentPoolSize + ", Total tickets left = " + remainingTickets);
                    if (remainingTickets <= 0 && currentPoolSize == 0) {
                        System.out.println("All tickets sold out!");
                        break;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitorThread.start();

        // Wait for threads to complete
        try {
            vendor1.join();
            vendor2.join();
            customer1.join();
            customer2.join();
            customer3.join();
            monitorThread.join();
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("Simulation completed.");
    }
}
