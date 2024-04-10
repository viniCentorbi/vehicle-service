package com.api.parkingcontrol.builder.entity.vehicle;

import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;

public class VehicleEntityBuilder {

    public VehicleEntity getCarEntity(VehicleDto dto){
        return VehicleEntity.builder()
                .id(dto.getId())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .color(dto.getColor())
                .plate(dto.getPlate())
                .type(dto.getType())
                .build();
    }
}
