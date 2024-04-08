package com.api.parkingcontrol.mapper.vehicle;

import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleEntity dtoToEntity(VehicleDto dto);

    VehicleDto entityToDto(VehicleEntity entity);
}
