package com.api.parkingcontrol.enums.vehicle;

import lombok.Getter;

@Getter
public enum EnumVehicleType {

    CAR(1),
    MOTORCYCLE(2);

    private final int id;

    EnumVehicleType(int id){
        this.id = id;
    }
}
