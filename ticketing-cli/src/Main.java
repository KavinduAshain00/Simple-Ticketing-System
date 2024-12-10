import config.Configuration;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static config.Configuration.logger;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration config = null;

        System.out.println("Welcome to the Real-Time Ticketing System!");
        System.out.println("1. Load an existing configuration");
        System.out.println("2. Create a new configuration");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            String jsonPath = "config.json"; // Always load from this path
            config = Configuration.loadFromJson(jsonPath);

            if (config != null) {
                System.out.println("Configuration loaded successfully.");
            } else {
                System.out.println("Failed to load configuration from " + jsonPath + ". Exiting.");
                System.exit(0);
            }
        } else if (choice == 2) {
            config = new Configuration();

            System.out.print("Enter total tickets: ");
            config.setTotalTickets(scanner.nextInt());

            System.out.print("Enter maximum pool size: ");
            config.setMaxPoolSize(scanner.nextInt());

            System.out.print("Enter vendor release time (in ms): ");
            config.setVendorReleaseTime(scanner.nextInt());

            System.out.print("Enter customer buy time (in ms): ");
            config.setCustomerBuyTime(scanner.nextInt());

            System.out.println("Configuration created successfully.");

            // Save configurations in the current directory
            String jsonPath = "config.json";
            String serPath = "config.ser";
            String txtPath = "config.txt";

            config.saveToJson(jsonPath);
            config.saveToSerialized(serPath);
            config.saveToText(txtPath);

            System.out.println("Configuration saved in the current directory as:");
            System.out.println(" - JSON: " + jsonPath);
            System.out.println(" - Serialized: " + serPath);
            System.out.println(" - Text: " + txtPath);
        } else {
            System.out.println("Invalid choice. Exiting.");
            System.exit(0);
        }

        // Simulation starts here
        System.out.println("\nStarting simulation with the following configuration:");
        System.out.println("Total Tickets: " + config.getTotalTickets());
        System.out.println("Maximum Pool Size: " + config.getMaxPoolSize());
        System.out.println("Vendor Release Time: " + config.getVendorReleaseTime() + " ms");
        System.out.println("Customer Buy Time: " + config.getCustomerBuyTime() + " ms");
        logger.info("Simulation will start in 2 seconds...");

        // Delay simulation start
        try {
            Thread.sleep(2000); // 2-second delay
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        // Create ticket pool and participants
        TicketPool pool = new TicketPool(config.getMaxPoolSize());
        int totalTickets = config.getTotalTickets();

        Vendor vendor1 = new Vendor("Vendor 1", pool, config.getVendorReleaseTime(), totalTickets / 2);
        Vendor vendor2 = new Vendor("Vendor 2", pool, config.getVendorReleaseTime(), totalTickets / 2);

        Customer customer1 = new Customer("Customer 1", pool, config.getCustomerBuyTime());
        Customer customer2 = new Customer("Customer 2", pool, config.getCustomerBuyTime());
        Customer customer3 = new Customer("Customer 3", pool, config.getCustomerBuyTime());

        // Run simulation using threads
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(vendor1);
        executor.execute(vendor2);
        executor.execute(customer1);
        executor.execute(customer2);
        executor.execute(customer3);

        logger.info("Simulation started. Check logs for real-time updates.");
    }
}
