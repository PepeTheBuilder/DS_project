package org.example.proiect_ds_devices.Controller;

import org.example.proiect_ds_devices.Entity.Device;
import org.example.proiect_ds_devices.Services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "http://localhost:4200")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    private static final Logger LOGGER = Logger.getLogger(DeviceController.class.getName());

    public DeviceController() {}

    @PostMapping("/bind")
    public ResponseEntity<Device> registerDevice(@RequestBody Device device) {
        LOGGER.info("Binding new device: " + device.toString() + " to user: " + device.getEmail());
        Device savedDevice = deviceService.save(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.findAll();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/getDeviceById")
    public ResponseEntity<Device> getDeviceById(@RequestBody long id) {
        Device device = deviceService.findById(id);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(device);
    }

    @PutMapping("/update")
    public ResponseEntity<Device> updateDevice(@RequestBody Device device) {
        if(device.getId()== 0 && device.getEmail() != null){
            registerDevice(device);
            return ResponseEntity.ok(device);
        }
        Device updatedDevice = deviceService.update(device);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteDevice(@RequestBody Long id) {
        Device deviceToDelete = deviceService.findById(id);
        if (deviceToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        deviceService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getDeviceByName")
    public ResponseEntity<Device> getDeviceByName(@RequestBody String name) {
        Device device = deviceService.findByName(name);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(device);
    }

    @GetMapping("/getDevicesByEmail")
    public ResponseEntity<List<Device>> getDevicesByEmail(@RequestParam String email) {
        List<Device> devices = deviceService.findByEmail(email);
        if(devices.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(devices);
    }
    @GetMapping("/isOn")
    public boolean isOn() {
        return true;
    }

}
