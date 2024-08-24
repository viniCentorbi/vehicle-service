package com.api.parkingcontrol.service.vehicle;

import com.api.parkingcontrol.model.dto.page.ResponsePageDto;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;

import java.util.UUID;

public interface VehicleService {

    VehicleDto save(VehicleDto vehicleDto);

    void delete(UUID id);

    VehicleDto findById(UUID id);

    VehicleDto update(VehicleDto dto);

    ResponsePageDto<VehicleDto> findAll(int pageNumber, int pageSize);
}
