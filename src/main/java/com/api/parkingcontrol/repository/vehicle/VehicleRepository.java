package com.api.parkingcontrol.repository.vehicle;

import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {

    @Query("SELECT v FROM VehicleEntity v WHERE v.type = ?1")
    Page<VehicleEntity> findAllByType(int type, Pageable pageable);
}
