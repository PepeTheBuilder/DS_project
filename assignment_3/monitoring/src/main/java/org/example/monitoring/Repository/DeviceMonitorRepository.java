package org.example.monitoring.Repository;

import org.example.monitoring.Entity.DeviceMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMonitorRepository extends JpaRepository<DeviceMonitor, Long> {
    List<DeviceMonitor> findByDeviceId(Long deviceId);

    List<DeviceMonitor> findByDeviceIdAndTimestampBetween(Long deviceId, Long startTimestamp, Long endTimestamp);
}
