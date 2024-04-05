package com.api.parkingcontrol.repository.vehicle;

import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {

}
