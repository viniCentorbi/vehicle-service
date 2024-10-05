package com.api.vehicle.mapper.page;

import com.api.vehicle.model.dto.VehicleDto;
import com.api.vehicle.model.entity.VehicleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehiclePageMapper extends BasePageMapper<VehicleDto, VehicleEntity> {

}
