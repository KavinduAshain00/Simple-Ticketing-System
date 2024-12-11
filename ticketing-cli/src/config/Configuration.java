package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    private final int totalTicketCount;
    private final int maxPoolSize;
    private final int vendorReleaseTime;
    private final int customerBuyingTime;

    public Configuration(int totalTicketCount, int maxPoolSize, int vendorReleaseTime, int customerBuyingTime) {
        this.totalTicketCount = totalTicketCount;
        this.maxPoolSize = maxPoolSize;
        this.vendorReleaseTime = vendorReleaseTime;
        this.customerBuyingTime = customerBuyingTime;
        logger.info("Configuration created: " + this);
    }

    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getVendorReleaseTime() {
        return vendorReleaseTime;
    }

    public int getCustomerBuyingTime() {
        return customerBuyingTime;
    }

    public void saveToFile() throws IOException {
        String jsonFilePath = "configuration.json";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);

        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write(json);
            logger.info("Configuration saved to " + jsonFilePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving configuration to file", e);
            throw e;
        }
    }

    public static Configuration loadFromFile() throws IOException {
        String filePath = "configuration.json";
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Configuration config = gson.fromJson(reader, Configuration.class);
            logger.info("Configuration loaded from " + filePath);
            return config;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration from file", e);
            throw e;
        }
    }

    public void saveToTextFile() throws IOException {
        String textFilePath = "configuration.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(textFilePath))) {
            writer.println("Total Ticket Count: " + totalTicketCount);
            writer.println("Max Pool Size: " + maxPoolSize);
            writer.println("Vendor Release Time (ms): " + vendorReleaseTime);
            writer.println("Customer Buying Time (ms): " + customerBuyingTime);
            logger.info("Configuration saved to " + textFilePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving configuration to text file", e);
            throw e;
        }
    }

    public void saveToSerializedFile() throws IOException {
        String serializedFilePath = "configuration.ser";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serializedFilePath))) {
            oos.writeObject(this);
            logger.info("Configuration saved to " + serializedFilePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving configuration to serialized file", e);
            throw e;
        }
    }

    public static Configuration createNewConfiguration() {
        Scanner scanner = new Scanner(System.in);
        logger.info("Creating a new configuration...");

        System.out.print("Enter total ticket count: ");
        int totalTicketCount = scanner.nextInt();

        System.out.print("Enter maximum pool size: ");
        int maxPoolSize = scanner.nextInt();

        System.out.print("Enter vendor release time (ms): ");
        int vendorReleaseTime = scanner.nextInt();

        System.out.print("Enter customer buying time (ms): ");
        int customerBuyingTime = scanner.nextInt();

        Configuration config = new Configuration(totalTicketCount, maxPoolSize, vendorReleaseTime, customerBuyingTime);
        logger.info("New configuration created: " + config);
        return config;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "totalTicketCount=" + totalTicketCount +
                ", maxPoolSize=" + maxPoolSize +
                ", vendorReleaseTime=" + vendorReleaseTime +
                ", customerBuyingTime=" + customerBuyingTime +
                '}';
    }
}
