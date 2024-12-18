package org.example.proiect_ds_devices.Entity;

import ch.qos.logback.core.joran.spi.DefaultClass;
import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "devices", schema = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String name;
    private String location;
    @Column(name = "email")
    private String email;
    private long maxkwh;

    public Device() {
    }

    public Device(String name, String location, String email, long maxkwh) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.maxkwh = maxkwh;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public long getMaxkwh() {
        return maxkwh;
    }

    public void setMaxkwh(long maxkwh) {
        this.maxkwh = maxkwh;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", username='" + name + '\'' +
                ", location='" + location + '\'' +
                ", maxkwh=" + maxkwh +
                ", userEmail='" + email + '\'' +
                '}';
    }
}
