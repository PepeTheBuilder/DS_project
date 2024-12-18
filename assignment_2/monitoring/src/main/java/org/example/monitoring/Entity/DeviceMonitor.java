package org.example.monitoring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "deviceMonitor")
public class DeviceMonitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    private long deviceId;
    private long timestamp;
    private long maxkwh;
    private float kwh;

    public DeviceMonitor() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMaxkwh() {
        return maxkwh;
    }

    public void setMaxkwh(long maxkwh) {
        this.maxkwh = maxkwh;
    }

    public float getKwh() {
        return kwh;
    }

    public void setKwh(float kwh) {
        this.kwh = kwh;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }
}
