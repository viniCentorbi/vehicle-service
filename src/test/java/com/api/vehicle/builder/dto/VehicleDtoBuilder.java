package com.api.vehicle.builder.dto;

import com.api.vehicle.enums.type.EnumVehicleType;
import com.api.vehicle.model.dto.VehicleDto;
import com.api.vehicle.model.entity.VehicleEntity;

import java.util.UUID;

public class VehicleDtoBuilder {

    public VehicleDto getCarDto(UUID id){
        return VehicleDto.builder()
                .id(id)
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF1234")
                .type(EnumVehicleType.CAR.getId())
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
                .type(EnumVehicleType.CAR.getId())
                .build();
    }

    public VehicleDto getVehicle(UUID id, EnumVehicleType type){
        return VehicleDto.builder()
                .id(id)
                .brand("Ford")
                .model("Fiesta")
                .color("Preto")
                .plate("EAT1346")
                .type(type.getId())
                .build();
    }

    public VehicleDto getVehicle(EnumVehicleType type){
        return VehicleDto.builder()
                .id(null)
                .brand("Marca")
                .model("Modelo")
                .color("Preto")
                .plate("EAT1346")
                .type(type.getId())
                .build();
    }
}
