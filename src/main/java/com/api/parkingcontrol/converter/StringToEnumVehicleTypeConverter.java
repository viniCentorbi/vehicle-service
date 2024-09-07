package com.api.parkingcontrol.converter;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import com.api.parkingcontrol.enums.vehicle.EnumVehicleType;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumVehicleTypeConverter implements Converter<String, EnumVehicleType> {

    @Override
    public EnumVehicleType convert(String source) {
        try{
            return EnumVehicleType.fromId(Integer.parseInt(source));
        } catch (IllegalArgumentException e){
            throw new ConversionFailedException(
                    TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(EnumVehicleType.class),
                    source, e);
        }
    }
}
