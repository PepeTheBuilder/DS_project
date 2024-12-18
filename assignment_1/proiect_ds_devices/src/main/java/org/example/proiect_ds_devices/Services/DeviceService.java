package org.example.proiect_ds_devices.Services;

import org.example.proiect_ds_devices.Entity.Device;
import org.example.proiect_ds_devices.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public Device findById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    public Device update(Device device) {
        return deviceRepository.save(device);
    }

    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }

    public Device findByName(String device_name) {
        return deviceRepository.findByName(device_name);
    }

    public List<Device> findByEmail(String email) {
        return deviceRepository.findByEmail(email);
    }
}
