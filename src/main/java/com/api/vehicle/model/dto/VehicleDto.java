package com.api.vehicle.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class VehicleDto {

    private UUID id;

    @Schema(example = "Mitsubishi")
    @NotBlank
    private String brand;

    @Schema(example = "Pajero Sport SE 3.0 4x2 V6 177cv Aut.")
    @NotBlank
    private String model;

    @Schema(example = "Vermelho")
    @NotBlank
    private String color;

    @Schema(example = "NAA4117")
    @NotBlank
    @Size(min = 7, max = 7)
    private String plate;

    @Schema(example = "1", description = "1-CAR, 2-MOTORCYCLE")
    @Positive
    private int type;
}
