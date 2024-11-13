package com.example.ticketing_system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TicketingSystemApplication {

	@Value("${server.port}")
    private String serverPort;

	public static void main(String[] args) {
		SpringApplication.run(TicketingSystemApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("Server is running on port " + serverPort);
    }

}
