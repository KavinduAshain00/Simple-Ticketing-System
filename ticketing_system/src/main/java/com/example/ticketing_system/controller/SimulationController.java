package com.example.ticketing_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketing_system.model.Configuration;
import com.example.ticketing_system.service.SimulationService;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "http://localhost:4200")
public class SimulationController {
    @Autowired
    private SimulationService simulationService;

    @PostMapping("/start")
    public ResponseEntity<Object>  startSimulation(@RequestBody Configuration config) {
        simulationService.startSimulation(config);
        System.out.println("simulation started");
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Simulation started!\"}");
    }
        
    
}

