import config.Configuration;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final ReentrantLock lock = new ReentrantLock();

    private static ScheduledExecutorService vendorScheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService customerScheduler = Executors.newSingleThreadScheduledExecutor();

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
            TicketPool pool = new TicketPool(0, config.getTotalTicketCount());

            // Reinitialize schedulers if they are shutdown
            if (vendorScheduler.isShutdown()) {
                vendorScheduler = Executors.newSingleThreadScheduledExecutor();
            }
            if (customerScheduler.isShutdown()) {
                customerScheduler = Executors.newSingleThreadScheduledExecutor();
            }

            // Vendor logic
            vendorScheduler.execute(() -> {
                int vendorId = 1;
                while (true) { // Check stop flag
                    lock.lock();
                    try {
                        if (pool.getRemainingTickets() > 0) {
                            new Vendor(vendorId, pool, config, lock).run();
                            vendorId = (vendorId % 2) + 1; // Switch between Vendor 1 and Vendor 2
                        } else {
                            logger.info("Vendor " + vendorId + " has no more tickets to add. Stopping.");
                            break;
                        }
                    } finally {
                        lock.unlock();
                    }
                    try {
                        Thread.sleep(config.getVendorReleaseTime());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Interrupt to stop the thread
                        break;
                    }
                }
            });

            // Customer logic
            customerScheduler.execute(() -> {
                int customerId = 1;
                while (true) { // Check stop flag
                    lock.lock();
                    try {
                        if (pool.getCurrentPoolSize() > 0 || pool.getRemainingTickets() > 0) {
                            new Customer(customerId, pool, config, lock).run();
                            customerId = (customerId % 3) + 1; // Switch between Customer 1, 2, and 3
                        } else {
                            logger.info("All tickets are sold. Simulation ending.");
                            break;
                        }
                    } finally {
                        lock.unlock();
                    }
                    try {
                        Thread.sleep(config.getCustomerBuyingTime());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Interrupt to stop the thread
                        break;
                    }
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
