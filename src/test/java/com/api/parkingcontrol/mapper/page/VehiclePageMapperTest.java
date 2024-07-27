package com.api.parkingcontrol.mapper.page;

import com.api.parkingcontrol.builder.dto.vehicle.VehicleDtoBuilder;
import com.api.parkingcontrol.builder.entity.vehicle.VehicleEntityBuilder;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.dto.vehicle.page.ResponseVehiclePageDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.UUID;

@SpringBootTest
class VehiclePageMapperTest {

    private final VehiclePageMapper mapper;

    private final VehicleDtoBuilder dtoBuilder;
    private final VehicleEntityBuilder entityBuilder;

    @Autowired
    public VehiclePageMapperTest(VehiclePageMapper mapper) {
        this.mapper = mapper;
        this.dtoBuilder = new VehicleDtoBuilder();
        this.entityBuilder = new VehicleEntityBuilder();
    }

    @Test
    void should_MapAllFieldsCorrectly_When_MapToResponseVehiclePageDto(){

        VehicleEntity firstEntity = entityBuilder.getCarEntity(UUID.randomUUID());
        VehicleEntity secondEntity = entityBuilder.getCarEntity(UUID.randomUUID());
        VehicleEntity thirdEntity = entityBuilder.getCarEntity(UUID.randomUUID());

        List<VehicleDto> expectedList = List.of(dtoBuilder.getCarDto(firstEntity), dtoBuilder.getCarDto(secondEntity),
                dtoBuilder.getCarDto(thirdEntity));

        ResponseVehiclePageDto expected = ResponseVehiclePageDto.builder()
                .currentPage(0)
                .pageSize(2)
                .totalItems(3)
                .totalPages(2)
                .listVehicleDto(expectedList).build();

        Page<VehicleEntity> pageEntity = new PageImpl<>(List.of(firstEntity, secondEntity, thirdEntity),
                PageRequest.of(expected.getCurrentPage(), expected.getPageSize()), expected.getTotalItems());

        ResponseVehiclePageDto actual = this.mapper.pageEntityToPageDto(pageEntity);
        assertThat(actual).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }
}
