package org.example.monitoring;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String DEVICE_UPDATE_QUEUE = "simulation";

    // CloudAMQP URL (replace with your actual CloudAMQP URI)
    private static final String CLOUDAMQP_URL = "amqps://alxniurm:SPdr5PbHiAbU4Yz0yFxBNLM6K3whccXY@dog.lmq.cloudamqp.com/alxniurm";

    @Bean
    public ConnectionFactory connectionFactory() {
        // Create a new connection factory
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(CLOUDAMQP_URL); // Set the CloudAMQP URI
        factory.setConnectionTimeout(30000); // Optional: Set connection timeout
        return factory;
    }

    @Bean
    public Queue deviceUpdateQueue() {
        return new Queue(DEVICE_UPDATE_QUEUE, true); // Durable queue
    }
}
