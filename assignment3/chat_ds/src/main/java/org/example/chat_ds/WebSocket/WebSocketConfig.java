package org.example.chat_ds.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple message broker for handling subscriptions
        config.enableSimpleBroker("/topic", "/queue");
        // Prefix for messages sent from the client to the application
        config.setApplicationDestinationPrefixes("/app");

//        System.out.println("Message broker configured");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define the WebSocket endpoint that the clients will connect to
        registry.addEndpoint("/api/chat")
                .setAllowedOrigins(
                        "http://localhost:3600",
                        "http://localhost:4200",
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "http://localhost:8082",
                        "http://localhost:8083"
                )
                .withSockJS(); // Enable SockJS fallback options
//        System.out.println("WebSocket endpoint registered: /api/chat");
    }

}