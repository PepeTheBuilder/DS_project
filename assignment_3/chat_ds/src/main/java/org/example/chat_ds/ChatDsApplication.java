package org.example.chat_ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"org.example.chat_ds.controller","org.example.chat_ds.Message", "org.example.chat_ds.WebSocket"})
@EntityScan("org.example.chat_ds.WebSocket")
@SpringBootApplication
public class ChatDsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatDsApplication.class, args);
    }

}
