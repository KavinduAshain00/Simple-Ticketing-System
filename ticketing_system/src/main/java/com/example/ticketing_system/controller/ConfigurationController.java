package com.example.ticketing_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketing_system.model.Configuration;
import com.example.ticketing_system.service.ConfigurationService;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping
    public Configuration saveConfiguration(@RequestBody Configuration config) {
        return configurationService.saveConfiguration(config);
    }

    @GetMapping("/all")
    public List<Configuration> getAllConfigurations() {
        return configurationService.getAllConfigurations();
    }

    @GetMapping("/last")
    public Configuration getLastConfiguration() {
        return configurationService.getLastConfiguration();
    }
    
}

