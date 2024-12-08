package com.example.ticketing_system.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketing_system.config.RealTimeUpdateHandler;

@RestController
@RequestMapping("/api/updates")
public class RealTimeUpdateController {

    private final RealTimeUpdateHandler updateHandler;

    public RealTimeUpdateController(RealTimeUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @PostMapping("/send-update")
    public void sendUpdate(@RequestBody String message) {
        updateHandler.sendUpdate(message);
    }
}
