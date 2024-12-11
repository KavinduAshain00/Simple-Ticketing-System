package com.example.ticketing_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing_system.Repositories.ConfigurationRepository;
import com.example.ticketing_system.model.Configuration;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configRepository;

    // Method to get all configurations
    public List<Configuration> getAllConfigurations() {
        return configRepository.findAll();
    }

    // Method to get the last configuration based on ID
    public Configuration getLastConfiguration() {
        return configRepository.findAll()
                .stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public Configuration saveConfiguration(Configuration config) {
        return configRepository.save(config);
    }
}
