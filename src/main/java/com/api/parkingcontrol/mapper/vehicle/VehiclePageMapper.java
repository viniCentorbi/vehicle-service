package com.api.parkingcontrol.mapper.vehicle;

import com.api.parkingcontrol.mapper.page.BasePageMapper;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehiclePageMapper extends BasePageMapper<VehicleDto, VehicleEntity> {

}
