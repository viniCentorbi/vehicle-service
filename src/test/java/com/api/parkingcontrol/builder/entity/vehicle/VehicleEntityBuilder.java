package com.api.parkingcontrol.builder.entity.vehicle;

import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;

import java.util.UUID;

public class VehicleEntityBuilder {

    public VehicleEntity getCarEntity(UUID id){
        return VehicleEntity.builder()
                .id(id)
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC1234")
                .type(1)
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
