package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.Scanner;

public class Configuration {
    private int totalTicketCount;
    private int maxPoolSize;
    private int vendorReleaseTime;
    private int customerBuyingTime;

    public Configuration(int totalTicketCount, int maxPoolSize, int vendorReleaseTime, int customerBuyingTime) {
        this.totalTicketCount = totalTicketCount;
        this.maxPoolSize = maxPoolSize;
        this.vendorReleaseTime = vendorReleaseTime;
        this.customerBuyingTime = customerBuyingTime;
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
            System.out.println("Configuration saved to " + jsonFilePath);
        }
    }

    public static Configuration loadFromFile() throws IOException {
        String filePath = "configuration.json";
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Configuration config = gson.fromJson(reader, Configuration.class);
            System.out.println("Configuration loaded from " + filePath);
            return config;
        }
    }

    public static Configuration createNewConfiguration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Creating a new configuration...");

        System.out.print("Enter total ticket count: ");
        int totalTicketCount = scanner.nextInt();

        System.out.print("Enter maximum pool size: ");
        int maxPoolSize = scanner.nextInt();

        System.out.print("Enter vendor release time (ms): ");
        int vendorReleaseTime = scanner.nextInt();

        System.out.print("Enter customer buying time (ms): ");
        int customerBuyingTime = scanner.nextInt();

        return new Configuration(totalTicketCount, maxPoolSize, vendorReleaseTime, customerBuyingTime);
    }
}