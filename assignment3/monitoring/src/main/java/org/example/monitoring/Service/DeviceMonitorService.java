package org.example.monitoring.Service;

import org.example.monitoring.Entity.DeviceMonitor;
import org.example.monitoring.Repository.DeviceMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceMonitorService {
    @Autowired
    private DeviceMonitorRepository deviceMonitorRepository;

    public void save(DeviceMonitor deviceMonitor) {
        deviceMonitorRepository.save(deviceMonitor);
    }

    public List<DeviceMonitor> findByDeviceId(Long deviceId) {
        return deviceMonitorRepository.findByDeviceId(deviceId);
    }

    public List<DeviceMonitor> findAll() {
        return deviceMonitorRepository.findAll();
    }

    public DeviceMonitor findById(Long id) {
        return deviceMonitorRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        deviceMonitorRepository.deleteById(id);
    }

    public List<DeviceMonitor> findByDeviceIdAndTimestampBetween(Long deviceId, Long startTimestamp, Long endTimestamp) {
        return deviceMonitorRepository.findByDeviceIdAndTimestampBetween(deviceId, startTimestamp, endTimestamp);
    }
}
