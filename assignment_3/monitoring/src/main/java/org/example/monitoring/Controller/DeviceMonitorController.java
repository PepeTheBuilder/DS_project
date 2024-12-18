package org.example.monitoring.Controller;

import org.example.monitoring.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.monitoring.Entity.DeviceMonitor;
import org.example.monitoring.Repository.DeviceMonitorRepository;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin
public class DeviceMonitorController {
    @Autowired
    private DeviceMonitorRepository deviceMonitorRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ArrayList<Integer> listMaxKwh = new ArrayList<Integer>();

    @RabbitListener(queues = RabbitMQConfig.DEVICE_UPDATE_QUEUE)
    public void updateDevice(String message) {
        // Example message: "{timestamp: 118800; deviceId: 2; energy: 2334.46;}"
        // Remove the curly braces and split by semicolon
        System.out.println("Received message: " + message);
        String cleanMessage = message.replace("{", "").replace("}", "").trim();
        String[] parts = cleanMessage.split(";");

        // Parse the message parts
        long timestamp = 0;
        int deviceId = 0;
        float energy = 0;

        for (String part : parts) {
            String[] keyValue = part.split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case "timestamp":
                    timestamp = Long.parseLong(value);
                    break;
                case "deviceId":
                    deviceId = Integer.parseInt(value);
                    break;
                case "energy":
                    energy = Float.parseFloat(value);
                    break;
            }
        }

        if( listMaxKwh.size() >= deviceId){
            if( listMaxKwh.get(deviceId) <= 0 ) {
                listMaxKwh.set(deviceId, getMaxKwh(deviceId));
            }
        }
        else {
            while( listMaxKwh.size() < deviceId){
                listMaxKwh.add(0);
            }
            listMaxKwh.add(getMaxKwh(deviceId));
        }

        DeviceMonitor record = new DeviceMonitor();

        // Set the energy or other relevant fields (replace setKwh with your actual field name)
        record.setKwh(energy); // Assuming `setEnergy` exists to update energy
        record.setDeviceId(deviceId); // Assuming `setId` exists to update device ID
        record.setTimestamp(timestamp); // Assuming `setTimestamp` exists to update timestamp
        record.setMaxkwh(listMaxKwh.get(deviceId));

        deviceMonitorRepository.save(record);

        // Check if alert needs to be sent (based on your energy threshold logic)
        if (energy > record.getMaxkwh()) { // Assuming `getMaxEnergy` is the threshold field
            sendAlert(record);
        }
    }

    private void sendAlert(DeviceMonitor device) {
        // Send alert to the user
        System.out.println("ALERT: Device " + device.getDeviceId() + " has exceeded the maximum kwh limit at " + device.getTimestamp() + " with " + device.getKwh() + " kwh.");
    }

    private int getMaxKwh(int id) {
        try {
            String apiUrl = "http://localhost/api/devices/getMaxKwh/" + id;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch maxKwh. HTTP error code: " + responseCode);
                return -1;
            }

            // Read the response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response = reader.readLine();
                return Integer.parseInt(response.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/seeChart")
    public ResponseEntity<List<Float>> seeChart(@RequestParam long id, @RequestParam long day) {
        final long OneDayInSec = 86400;
        List<DeviceMonitor> devices = deviceMonitorRepository.findByDeviceIdAndTimestampBetween(id, day*OneDayInSec, (day+1)*OneDayInSec);
        if (devices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Float> kwh = new ArrayList<>();
        for (DeviceMonitor device : devices) {
            kwh.add(device.getKwh());
        }
        return ResponseEntity.ok(kwh);
    }
}
