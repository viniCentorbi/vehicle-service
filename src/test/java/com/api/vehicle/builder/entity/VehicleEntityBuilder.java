package com.api.vehicle.builder.entity;

import com.api.vehicle.enums.type.EnumVehicleType;
import com.api.vehicle.model.dto.VehicleDto;
import com.api.vehicle.model.entity.VehicleEntity;

import java.util.UUID;

public class VehicleEntityBuilder {

    public VehicleEntity getCarEntity(UUID id){
        return VehicleEntity.builder()
                .id(id)
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC1234")
                .type(EnumVehicleType.CAR.getId())
                .build();
    }

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
