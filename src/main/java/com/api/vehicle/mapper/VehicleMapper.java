package com.api.vehicle.mapper;

import com.api.vehicle.model.dto.vehicle.VehicleDto;
import com.api.vehicle.model.entity.vehicle.VehicleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleEntity dtoToEntity(VehicleDto dto);

    VehicleDto entityToDto(VehicleEntity entity);
}
