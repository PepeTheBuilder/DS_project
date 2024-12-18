package org.example;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) {
        final String QUEUE_NAME = "simulation";
        final String CSV_PATH = "./sensor.csv";

        int deviceId = args.length == 1 && args[0].matches("\\d+") ? Integer.parseInt(args[0]) : 2;

        String cloudAMQPUrl = "amqps://alxniurm:SPdr5PbHiAbU4Yz0yFxBNLM6K3whccXY@dog.lmq.cloudamqp.com/alxniurm";

        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(cloudAMQPUrl);
            try (BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH));
                 var connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                String line;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null) {
                    try {
                        // Parse energy value from the CSV line
                        float energy = Float.parseFloat(line.trim());

                        // Create message payload
                        String message = String.format(
                                "{timestamp: %d; deviceId: %d; energy: %.2f;}",
                                (lineNumber * 600L), deviceId, energy
                        );

                        // Publish the message to the queue
                        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Sent: " + message);

                        Thread.sleep(100); // Simulate delay
                        lineNumber++;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in line: " + line);
                    }
                }
            }
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Failed to configure RabbitMQ: " + e.getMessage());
        }
    }
}
