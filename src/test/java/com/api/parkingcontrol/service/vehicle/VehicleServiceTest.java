package com.api.parkingcontrol.service.vehicle;

import com.api.parkingcontrol.builder.dto.vehicle.VehicleDtoBuilder;
import com.api.parkingcontrol.builder.entity.vehicle.VehicleEntityBuilder;
import com.api.parkingcontrol.exception.response.BadRequestException;
import com.api.parkingcontrol.exception.response.InternalServerErrorException;
import com.api.parkingcontrol.exception.response.NotFoundException;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import com.api.parkingcontrol.repository.vehicle.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class VehicleServiceTest {

    @MockBean
    private VehicleRepository mockVehicleRepository;
    @SpyBean
    private VehicleService spyService;

    private final VehicleService service;

    private final VehicleDtoBuilder dtoBuilder;
    private final VehicleEntityBuilder entityBuilder;

    @Autowired
    public VehicleServiceTest(VehicleService service) {
        this.service = service;
        this.dtoBuilder = new VehicleDtoBuilder();
        this.entityBuilder = new VehicleEntityBuilder();
    }

    @Test
    void should_ReturnSavedVehicle_When_Save() {
        UUID idDto = null;
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idDto);

        final UUID idExpected = UUID.randomUUID();

        VehicleEntity savedEntity = this.entityBuilder.getCarEntity(dtoExpected);
        savedEntity.setId(idExpected);

        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenReturn(savedEntity);

        VehicleDto dtoActual = this.service.save(dtoExpected);

        assertNotNull(dtoActual);
        assertEquals(idExpected, dtoActual.getId());
        assertThat(dtoActual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(dtoExpected);
    }

    @Test
    void should_ThrowsInternalServerErrorException_When_NotSave(){
        UUID idDto = null;
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idDto);

        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(InternalServerErrorException.class, () -> this.service.save(dtoExpected));
    }

    @Test
    void should_DontThrowsInternalServerErrorException_When_DeleteVehicle(){
        UUID idDto = null;
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idDto);

        doNothing().when(this.mockVehicleRepository).delete(any(VehicleEntity.class));
        assertDoesNotThrow(() -> this.service.delete(dtoExpected));
    }

    @Test
    void should_ThrowsInternalServerErrorException_When_NotDeleteVehicle(){
        UUID idDto = null;
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idDto);

        doThrow(OptimisticLockingFailureException.class).when(this.mockVehicleRepository).delete(any(VehicleEntity.class));
        assertThrows(InternalServerErrorException.class, () -> this.service.delete(dtoExpected));
    }

    @Test
    void should_ReturnVehicle_When_FindVehicle(){
        final UUID idExpected = UUID.randomUUID();

        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idExpected);
        Optional<VehicleEntity> optEntityFound = Optional.ofNullable(this.entityBuilder.getCarEntity(dtoExpected));

        when(this.mockVehicleRepository.findById(any(UUID.class))).thenReturn(optEntityFound);
        VehicleDto dtoActual = this.service.findById(idExpected);

        assertNotNull(dtoActual);
        assertThat(dtoActual).isEqualTo(dtoExpected);
    }

    @Test
    void should_ThrowsNotFoundException_When_NotFindVehicle(){
        UUID id = UUID.randomUUID();
        when(this.mockVehicleRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> this.service.findById(id));
    }

    @Test
    void should_ThrowsBadRequestException_When_IdIsNull(){
        when(this.mockVehicleRepository.findById(any())).thenThrow(IllegalArgumentException.class);
        assertThrows(BadRequestException.class, () -> this.service.findById(null));
    }

    @Test
    void should_ReturnUpdatedVehicle_When_VehicleExistsAndUpdates(){
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(UUID.randomUUID());

        VehicleDto foundVehicle = this.dtoBuilder.getCarDto(dtoExpected.getId());
        foundVehicle.setColor("Preto");

        VehicleEntity updatedEntity = this.entityBuilder.getCarEntity(dtoExpected);

        doReturn(foundVehicle).when(this.spyService).findById(any(UUID.class));
        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenReturn(updatedEntity);

        VehicleDto dtoActual = this.spyService.update(dtoExpected);

        assertThat(dtoActual).isEqualTo(dtoExpected);
    }

    @Test
    void should_ThrowsInternalServerErrorException_When_NotUpdateVehicle(){
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(UUID.randomUUID());

        VehicleDto foundVehicle = this.dtoBuilder.getCarDto(dtoExpected.getId());
        foundVehicle.setColor("Preto");

        doReturn(foundVehicle).when(this.spyService).findById(any(UUID.class));
        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(InternalServerErrorException.class, () -> this.spyService.update(dtoExpected));
    }
}
