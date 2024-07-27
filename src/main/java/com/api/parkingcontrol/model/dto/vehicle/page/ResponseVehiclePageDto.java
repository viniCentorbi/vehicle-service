package com.api.parkingcontrol.model.dto.vehicle.page;

import com.api.parkingcontrol.model.dto.page.ResponsePageDto;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseVehiclePageDto extends ResponsePageDto {
    private List<VehicleDto> listVehicleDto;
}
