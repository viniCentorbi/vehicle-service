package com.api.vehicle.mapper.vehicle;

import com.api.vehicle.mapper.page.BasePageMapper;
import com.api.vehicle.model.dto.vehicle.VehicleDto;
import com.api.vehicle.model.entity.vehicle.VehicleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehiclePageMapper extends BasePageMapper<VehicleDto, VehicleEntity> {

}
