package com.api.parkingcontrol.builder.dto.vehicle;

import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;

import java.util.UUID;

public class VehicleDtoBuilder {

    public VehicleDto getCarDto(UUID id){
        return VehicleDto.builder()
                .id(id)
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();
    }
}
