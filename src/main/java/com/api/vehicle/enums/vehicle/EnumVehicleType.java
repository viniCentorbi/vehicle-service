package com.api.vehicle.enums.vehicle;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum EnumVehicleType {

    CAR(1),
    MOTORCYCLE(2);

    private final Integer id;

    EnumVehicleType(Integer id){
        this.id = id;
    }

    public static EnumVehicleType fromId(Integer pId){
        return Stream.of(values())
                .filter(item -> item.id.equals(pId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + pId));
    }
}
