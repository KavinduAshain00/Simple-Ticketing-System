package com.example.ticketing_system.controller;

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

