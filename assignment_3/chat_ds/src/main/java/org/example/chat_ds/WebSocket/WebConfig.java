package org.example.chat_ds.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins( "http://localhost:3600",
                        "http://localhost:4200",
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "http://localhost:8082",
                        "http://localhost:8083")  // Allow your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow specific methods
//                .allowedHeaders("Access-Control-Allow-Credentials", "Content-Type", "Authorization")  // Allow specific headers
                .allowCredentials(false);
    }
}