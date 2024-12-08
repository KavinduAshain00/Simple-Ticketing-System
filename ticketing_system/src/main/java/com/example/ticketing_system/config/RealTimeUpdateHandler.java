package com.example.ticketing_system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class RealTimeUpdateHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeUpdateHandler.class);
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.info("New WebSocket connection established: {}", session.getId());
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
        // Process the message as needed (for example, you could send a response back to the client)
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        logger.info("Connection closed for session {}: {}", session.getId(), status.getReason());
        super.afterConnectionClosed(session, status);
    }

    // Broadcast update to all clients
    public void sendUpdate(String message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    session.sendMessage(new TextMessage(message));
                    logger.info("Sent update to session {}: {}", session.getId(), message);
                } catch (IOException e) {
                    logger.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
                }
            }
        }
    }

    // Optional: Send update to a specific client
    public void sendUpdateToClient(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
            logger.info("Sent message to session {}: {}", session.getId(), message);
        } catch (IOException e) {
            logger.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
        }
    }
}
