package config;

import java.io.*;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Configuration implements Serializable {
    private static final long serialVersionUID = 1L;
    private int totalTickets;
    private int maxPoolSize;
    private int vendorReleaseTime;
    private int customerBuyTime;
    public static final Logger logger = Logger.getLogger(Configuration.class.getName());

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getVendorReleaseTime() {
        return vendorReleaseTime;
    }

    public void setVendorReleaseTime(int vendorReleaseTime) {
        this.vendorReleaseTime = vendorReleaseTime;
    }

    public int getCustomerBuyTime() {
        return customerBuyTime;
    }

    public void setCustomerBuyTime(int customerBuyTime) {
        this.customerBuyTime = customerBuyTime;
    }

    // Save Configuration to JSON
    public void saveToJson(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
            logger.info("Configuration saved to JSON: " + fileName);
        } catch (IOException e) {
            logger.severe("Failed to save configuration to JSON: " + e.getMessage());
        }
    }

    // Load Configuration from JSON
    public static Configuration loadFromJson(String fileName) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            logger.severe("Failed to load configuration from JSON: " + e.getMessage());
            return null;
        }
    }

    // Save Configuration to Serialized (.ser)
    public void saveToSerialized(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            logger.info("Configuration saved to serialized file: " + fileName);
        } catch (IOException e) {
            logger.severe("Failed to save configuration to serialized file: " + e.getMessage());
        }
    }

    // Save Configuration to Plain Text (.txt)
    public void saveToText(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Total Tickets: " + totalTickets + "\n");
            writer.write("Max Pool Size: " + maxPoolSize + "\n");
            writer.write("Vendor Release Time: " + vendorReleaseTime + "ms\n");
            writer.write("Customer Buy Time: " + customerBuyTime + "ms\n");
            logger.info("Configuration saved to plain text: " + fileName);
        } catch (IOException e) {
            logger.severe("Failed to save configuration to plain text: " + e.getMessage());
        }
    }
}
