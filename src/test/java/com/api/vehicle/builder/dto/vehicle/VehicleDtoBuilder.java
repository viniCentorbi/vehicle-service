package com.api.vehicle.builder.dto.vehicle;

import com.api.vehicle.model.dto.vehicle.VehicleDto;
import com.api.vehicle.model.entity.vehicle.VehicleEntity;

import java.util.UUID;

public class VehicleDtoBuilder {

    public VehicleDto getCarDto(UUID id){
        return VehicleDto.builder()
                .id(id)
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF1234")
                .type(1)
                .build();
    }

    public VehicleDto getCarDto(VehicleEntity entity){
        return VehicleDto.builder()
                .id(entity.getId())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .plate(entity.getPlate())
                .type(entity.getType())
                .build();
    }

    public VehicleDto getCarPostDto(){
        return VehicleDto.builder()
                .id(null)
                .brand("Chevrolet")
                .model("Monsa")
                .color("Vinho")
                .plate("ESA1346")
                .type(1)
                .build();
    }
}