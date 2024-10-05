package com.api.vehicle.service.vehicle;

import com.api.vehicle.enums.type.EnumVehicleType;
import com.api.vehicle.model.dto.page.ResponsePageDto;
import com.api.vehicle.model.dto.vehicle.VehicleDto;

import java.util.UUID;

public interface VehicleService {

    VehicleDto save(VehicleDto vehicleDto);

    void delete(UUID id);

    VehicleDto findById(UUID id);

    VehicleDto update(VehicleDto dto);

    ResponsePageDto<VehicleDto> findAll(int pageNumber, int pageSize);

    ResponsePageDto<VehicleDto> findAllByType(EnumVehicleType vehicleType, int pageNumber, int pageSize);
}
