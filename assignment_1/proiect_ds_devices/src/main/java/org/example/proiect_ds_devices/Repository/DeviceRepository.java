package org.example.proiect_ds_devices.Repository;

import org.example.proiect_ds_devices.Entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByEmail(String email);

    Device findByName(String name);

}
