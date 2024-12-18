package org.example.chat_ds.WebSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.chat_ds.Message.ChatMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    // Store active WebSocket sessions by user (based on sender/receiver)
    private Map<String, WebSocketSession> activeSessions = new HashMap<>();

    // When a new message is received from the WebSocket
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Parse the message (the frontend sends a JSON message)
        System.out.println("Message received: " + message.getPayload());
        String payload = message.getPayload();

        // Example: message content looks like {"content": "Hello!", "sender": "user1", "receiver": "user2"}
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        // Process the message (send to the receiver if they are connected)
        String receiver = chatMessage.getReceiver();
        WebSocketSession receiverSession = activeSessions.get(receiver);

        if (receiverSession != null && receiverSession.isOpen()) {
            // Send the message to the receiver
            System.out.println("Sending message to receiver: " + receiver);
            receiverSession.sendMessage(new TextMessage(payload));
        } else {
            // Handle the case where the receiver is not connected
            System.out.println("Receiver not connected: " + receiver);
        }
    }

    // When a new WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Retrieve user info from headers or session
        String username = session.getUri().getQuery();
        if (username == null) {
            System.out.println("Invalid user: afterConnectionEstablished() " + session.getId());
            throw new IllegalStateException("Invalid user");
        }
        // Proceed with setting up the session
        super.afterConnectionEstablished(session);
    }

    // When a WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String user = session.getUri().getQuery();  // Example: ?user=username
        if (user != null) {
            activeSessions.remove(user);
            System.out.println("WebSocket connection closed: " + user);
        }
        else {
            System.out.println("Invalid user: after connection Closed id:" + session.getId());
            System.out.println("URI:"+session.getUri() + "\nattrib: " + session.getAttributes()+ "\nHandshake: " + session.getHandshakeHeaders()+ "\n status:" + status);
        }
    }

    // Get all active sessions
    public Map<String, WebSocketSession> getActiveSessions() {
        return activeSessions;
    }
}
