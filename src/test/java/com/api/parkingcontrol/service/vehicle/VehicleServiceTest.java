package com.api.parkingcontrol.service.vehicle;

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

    @Autowired
    public VehicleServiceTest(VehicleService service) {
        this.service = service;
    }

    @Test
    void should_ReturnSavedVehicle_When_Save() {
        VehicleDto dtoExpected = VehicleDto.builder()
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();

        final UUID idExpected = UUID.randomUUID();

        VehicleEntity savedEntity = VehicleEntity.builder()
                .id(idExpected)
                .brand(dtoExpected.getBrand())
                .model(dtoExpected.getModel())
                .color(dtoExpected.getColor())
                .plate(dtoExpected.getPlate())
                .type(dtoExpected.getType())
                .build();

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
        VehicleDto dtoExpected = VehicleDto.builder()
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();

        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(InternalServerErrorException.class, () -> this.service.save(dtoExpected));
    }

    @Test
    void should_DontThrowsInternalServerErrorException_When_DeleteVehicle(){
        VehicleDto dtoExpected = VehicleDto.builder()
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();

        doNothing().when(this.mockVehicleRepository).delete(any(VehicleEntity.class));
        assertDoesNotThrow(() -> this.service.delete(dtoExpected));
    }

    @Test
    void should_ThrowsInternalServerErrorException_When_NotDeleteVehicle(){
        VehicleDto dtoExpected = VehicleDto.builder()
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();

        doThrow(OptimisticLockingFailureException.class).when(this.mockVehicleRepository).delete(any(VehicleEntity.class));
        assertThrows(InternalServerErrorException.class, () -> this.service.delete(dtoExpected));
    }

    @Test
    void should_ReturnVehicle_When_FindVehicle(){
        final UUID idExpected = UUID.randomUUID();
        final String brandExpected = "Fiat";
        final String modelExpected = "Strada";
        final String colorExpected = "Vermelho";
        final String plateExpected = "RTF-1234";
        final int typeExpected = 1;

        Optional<VehicleEntity> optEntityFound = Optional.ofNullable(VehicleEntity.builder()
                .id(idExpected)
                .brand(brandExpected)
                .model(modelExpected)
                .color(colorExpected)
                .plate(plateExpected)
                .type(typeExpected)
                .build());

        when(this.mockVehicleRepository.findById(any(UUID.class))).thenReturn(optEntityFound);
        VehicleDto dtoActual = this.service.findById(idExpected);

        assertNotNull(dtoActual);
        assertEquals(idExpected, dtoActual.getId());
        assertEquals(brandExpected, dtoActual.getBrand());
        assertEquals(modelExpected, dtoActual.getModel());
        assertEquals(colorExpected, dtoActual.getColor());
        assertEquals(plateExpected, dtoActual.getPlate());
        assertEquals(typeExpected, dtoActual.getType());
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
        VehicleDto dtoExpected = VehicleDto.builder()
                .id(UUID.randomUUID())
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();

        VehicleDto foundVehicle = VehicleDto.builder()
                .id(dtoExpected.getId())
                .brand(dtoExpected.getBrand())
                .model(dtoExpected.getModel())
                .color(dtoExpected.getColor())
                .plate("Preto")
                .type(dtoExpected.getType())
                .build();

        VehicleEntity updatedEntity = VehicleEntity.builder()
                .id(dtoExpected.getId())
                .brand(dtoExpected.getBrand())
                .model(dtoExpected.getModel())
                .color(dtoExpected.getColor())
                .plate(dtoExpected.getPlate())
                .type(dtoExpected.getType())
                .build();

        doReturn(foundVehicle).when(this.spyService).findById(any(UUID.class));
        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenReturn(updatedEntity);

        VehicleDto dtoActual = this.spyService.update(dtoExpected);

        assertThat(dtoActual).isEqualTo(dtoExpected);
    }

    @Test
    void should_ThrowsInternalServerErrorException_When_NotUpdateVehicle(){
        VehicleDto dtoExpected = VehicleDto.builder()
                .id(UUID.randomUUID())
                .brand("Fiat")
                .model("Strada")
                .color("Vermelho")
                .plate("RTF-1234")
                .type(1)
                .build();

        VehicleDto foundVehicle = VehicleDto.builder()
                .id(dtoExpected.getId())
                .brand(dtoExpected.getBrand())
                .model(dtoExpected.getModel())
                .color("Preto")
                .plate(dtoExpected.getPlate())
                .type(dtoExpected.getType())
                .build();

        doReturn(foundVehicle).when(this.spyService).findById(any(UUID.class));
        when(this.mockVehicleRepository.save(any(VehicleEntity.class))).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(InternalServerErrorException.class, () -> this.spyService.update(dtoExpected));
    }
}
