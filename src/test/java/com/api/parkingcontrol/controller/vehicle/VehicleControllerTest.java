package com.api.parkingcontrol.controller.vehicle;

import com.api.parkingcontrol.builder.dto.vehicle.VehicleDtoBuilder;
import com.api.parkingcontrol.controller.VehicleController;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.service.vehicle.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class VehicleControllerTest {

    @MockBean
    private VehicleService mockService;

    private final VehicleController controller;
    private final VehicleDtoBuilder dtoBuilder;

    @Autowired
    public VehicleControllerTest(VehicleController controller) {
        this.controller = controller;
        this.dtoBuilder = new VehicleDtoBuilder();
    }

    @Test
    void should_ReturnVehicleDto_When_SaveVehicle(){
        VehicleDto postDto = this.dtoBuilder.getCarPostDto();

        VehicleDto dtoExpected = this.dtoBuilder.getCarPostDto();
        dtoExpected.setId(UUID.randomUUID());

        when(this.mockService.save(any(VehicleDto.class))).thenReturn(dtoExpected);

        ResponseEntity<VehicleDto> response = this.controller.save(postDto);

        assertNotNull(response);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(dtoExpected);
    }

    @Test
    void should_ReturnResponseEntityOk_When_SaveVehicle(){
        when(this.mockService.save(any(VehicleDto.class))).thenReturn(new VehicleDto());

        ResponseEntity<VehicleDto> response = this.controller.save(new VehicleDto());

        assertNotNull(response);
        assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @Test
    void should_ReturnVehicleDto_When_UpdateVehicle(){
        UUID idExpected = UUID.randomUUID();
        VehicleDto putDtoExpected = this.dtoBuilder.getCarDto(idExpected);

        when(this.mockService.update(any(VehicleDto.class))).thenReturn(putDtoExpected);

        ResponseEntity<VehicleDto> response = this.controller.update(putDtoExpected);

        assertNotNull(response);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(putDtoExpected);
    }

    @Test
    void should_ReturnResponseEntityOk_When_UpdateVehicle(){
        UUID idExpected = UUID.randomUUID();
        VehicleDto putDtoExpected = this.dtoBuilder.getCarDto(idExpected);

        when(this.mockService.update(any(VehicleDto.class))).thenReturn(putDtoExpected);

        ResponseEntity<VehicleDto> response = this.controller.update(putDtoExpected);

        assertNotNull(response);
        assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @Test
    void should_ReturnVehicleDto_When_FindVehicleById(){
        UUID idExpected = UUID.randomUUID();
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idExpected);

        when(this.mockService.findById(any(UUID.class))).thenReturn(dtoExpected);

        ResponseEntity<VehicleDto> response = this.controller.findById(idExpected);

        assertNotNull(response);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(dtoExpected);
    }

    @Test
    void should_ReturnResponseEntityOk_When_FindVehicleById(){
        when(this.mockService.findById(any(UUID.class))).thenReturn(new VehicleDto());

        ResponseEntity<VehicleDto> response = this.controller.findById(UUID.randomUUID());

        assertNotNull(response);
        assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @Test
    void should_ReturnResponseEntityNoContent_When_DeleteVehicle(){
        doNothing().when(this.mockService).delete(any(UUID.class));

        ResponseEntity<Void> response = this.controller.delete(UUID.randomUUID());

        assertNotNull(response);
        assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo((HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value())));
    }
}
