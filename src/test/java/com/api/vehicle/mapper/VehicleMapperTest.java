package com.api.vehicle.mapper;

import com.api.vehicle.builder.dto.VehicleDtoBuilder;
import com.api.vehicle.builder.entity.VehicleEntityBuilder;
import com.api.vehicle.model.dto.VehicleDto;
import com.api.vehicle.model.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VehicleMapperTest {

    private final VehicleMapper mapper;

    private final VehicleDtoBuilder dtoBuilder;
    private final VehicleEntityBuilder entityBuilder;

    @Autowired
    public VehicleMapperTest(VehicleMapper mapper) {
        this.mapper = mapper;
        this.dtoBuilder = new VehicleDtoBuilder();
        this.entityBuilder = new VehicleEntityBuilder();
    }

    @Test
    void should_MapAllFieldsCorrectly_When_MapToVehicleEntity(){
        VehicleDto dto = this.dtoBuilder.getCarDto(UUID.randomUUID());
        VehicleEntity entityExpected = this.entityBuilder.getCarEntity(dto);

        VehicleEntity entityActual = this.mapper.dtoToEntity(dto);

        assertThat(entityActual).isEqualTo(entityExpected);
    }

    @Test
    void should_MapAllFieldsCorrectly_When_MapToVehicleDto(){
        VehicleEntity entity = this.entityBuilder.getCarEntity(UUID.randomUUID());
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(entity);

        VehicleDto dtoActual = this.mapper.entityToDto(entity);

        assertThat(dtoActual).isEqualTo(dtoExpected);
    }
}
