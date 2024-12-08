package com.example.ticketing_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketing_system.Repositories.ConfigurationRepository;
import com.example.ticketing_system.model.Configuration;

@RestController
@RequestMapping("/api/config")
public class ConfigurationController {
    @Autowired
    private ConfigurationRepository configRepository;

    @PostMapping
    public Configuration saveConfiguration(@RequestBody Configuration config) {
        return configRepository.save(config);
    }

    @GetMapping
    public List<Configuration> getAllConfigurations() {
        return configRepository.findAll();
    }
}

