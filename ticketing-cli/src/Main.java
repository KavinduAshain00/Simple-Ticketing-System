import config.Configuration;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        configureLogger();

        Configuration config;

        logger.info("Welcome to the Real-Time Event Ticketing System.");
        System.out.println("1. Load existing configuration");
        System.out.println("2. Create new configuration");
        System.out.print("Enter your choice: ");

        int choice = new Scanner(System.in).nextInt();
        try {
            if (choice == 1) {
                config = Configuration.loadFromFile();
                logger.info("Configuration loaded successfully.");
                System.out.println("Loaded Configuration:\n" + config);
            } else if (choice == 2) {
                config = Configuration.createNewConfiguration();
                config.saveToFile();
                logger.info("New configuration created and saved.");
                System.out.println("Created Configuration:\n" + config);
            } else {
                logger.warning("Invalid choice. Exiting.");
                return;
            }

            // Initialize ticket pool
            TicketPool ticketPool = new TicketPool(config.getMaxPoolSize(), config.getTotalTicketCount());
            logger.info("Ticket pool initialized with max pool size: " + config.getMaxPoolSize() + ", total tickets: " + config.getTotalTicketCount());

            // Create thread pool
            ExecutorService executor = Executors.newCachedThreadPool();

            // Create and start vendor threads
            for (int i = 1; i <= 2; i++) {
                executor.execute(new Vendor(ticketPool, i, config.getVendorReleaseTime()));
            }

            // Create and start customer threads
            for (int i = 1; i <= 3; i++) {
                executor.execute(new Customer(ticketPool, i, config.getCustomerBuyingTime()));
            }

            executor.shutdown();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error handling configuration: " + e.getMessage(), e);
        }
    }

    private static void configureLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.info("Logger configured.");
    }
}
