package org.example.chat_ds.controller;

import org.example.chat_ds.Message.ChatMessage;
import org.example.chat_ds.WebSocket.CustomWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@RestController
public class ChatController {

    private final CustomWebSocketHandler webSocketHandler;

    @Autowired
    public ChatController(CustomWebSocketHandler customWebSocketHandler) {
        this.webSocketHandler = customWebSocketHandler;
    }

    @MessageMapping("/api/chat/send")
    @SendTo("/topic/messages") // The destination where the message will be sent
    public ChatMessage sendMessage(ChatMessage message) {
        System.out.println("Message received: " + message.getContent() + " from: " + message.getSender() + " to: " + message.getReceiver());
        // Send the message via WebSocket to the appropriate user
        String receiver = message.getReceiver();
        WebSocketSession receiverSession = webSocketHandler.getActiveSessions().get(receiver);

        if (receiverSession != null && receiverSession.isOpen()) {
            try {
                // Send the message to the receiver via WebSocket, and tell from whom the message is
                TextMessage textMessage = new TextMessage(message.getContent() + " --from:" + message.getSender());
                receiverSession.sendMessage(textMessage);
                System.out.println("Message sent to receiver: " + receiver + " - " + message.getContent() + " --from:" + message.getSender() ); ;
            } catch (IOException e) {
                System.out.println("Error sending message to receiver: " + receiver);
                e.printStackTrace();
            }
        } else {
            System.out.println("Receiver not connected: " + receiver);
        }

        return message; // Return the message to send to the destination
    }

    @GetMapping("/isOn")
    public String isOn() {
        return "Chat is on";
    }
}
