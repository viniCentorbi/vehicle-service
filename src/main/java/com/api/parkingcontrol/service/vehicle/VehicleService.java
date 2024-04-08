package com.api.parkingcontrol.service.vehicle;

import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;

import java.util.UUID;

public interface VehicleService {

    VehicleDto save(VehicleDto vehicleDto);

    void delete(VehicleDto vehicleDto);

    VehicleDto findById(UUID id);

    VehicleDto update(VehicleDto dto);
}
