package com.api.parkingcontrol.converter;

import com.api.parkingcontrol.enums.vehicle.EnumVehicleType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionFailedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class StringToEnumVehicleTypeConverterTest {

    @ParameterizedTest
    @EnumSource(EnumVehicleType.class)
    void given_ValidVehicleType_when_Convert_then_ReturnVehicleType(EnumVehicleType vehicleType){
        StringToEnumVehicleTypeConverter converter = new StringToEnumVehicleTypeConverter();
        EnumVehicleType actual = converter.convert(vehicleType.getId().toString());
        assertThat(actual).isNotNull().isEqualTo(vehicleType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10", "", " ", "anyText"})
    void given_InvalidVehicleType_when_Convert_then_ThrowsConversionFailedException(String invalidType){
        StringToEnumVehicleTypeConverter converter = new StringToEnumVehicleTypeConverter();
        assertThrows(ConversionFailedException.class, () -> converter.convert(invalidType));
    }
}
