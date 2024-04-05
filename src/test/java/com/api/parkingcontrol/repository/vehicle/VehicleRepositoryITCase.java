package com.api.parkingcontrol.repository.vehicle;

import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VehicleRepositoryITCase {

    private final VehicleRepository repository;

    @Autowired
    public VehicleRepositoryITCase(VehicleRepository repository){
        this.repository = repository;
    }

    @Transactional
    @Rollback
    @Test
    void should_SaveVehicle_When_IdIsNotDefined(){
        VehicleEntity entityExpected = VehicleEntity.builder()
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC-1234")
                .type(1)
                .build();

        VehicleEntity entityActual = this.repository.save(entityExpected);
        assertNotNull(entityActual);
        assertThat(entityActual).isEqualTo(entityExpected);
    }

    @Transactional
    @Rollback
    @Test
    void should_UpdateVehicle_When_IdIsDefinedAndVehicleExists(){
        VehicleEntity entitySaved = VehicleEntity.builder()
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC-1234")
                .type(1)
                .build();
        this.repository.save(entitySaved);

        VehicleEntity entityExpected = VehicleEntity.builder()
                .id(entitySaved.getId())
                .brand("Ford")
                .model("Fiesta")
                .color("Preto")
                .plate("ABC-1235")
                .type(1)
                .build();

        VehicleEntity entityActual = this.repository.save(entityExpected);

        assertNotNull(entityActual);
        assertEquals(entitySaved.getId(), entityActual.getId());
        assertThat(entityActual).isEqualTo(entityExpected);
    }

    @Transactional
    @Rollback
    @Test
    void should_RemoveVehicle_When_VehicleExists(){
        VehicleEntity entitySaved = VehicleEntity.builder()
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC-1234")
                .type(1)
                .build();
        this.repository.save(entitySaved);
        this.repository.delete(entitySaved);

        Optional<VehicleEntity> entityAcual = this.repository.findById(entitySaved.getId());
        assertTrue(entityAcual.isEmpty());
    }

    @Transactional
    @Rollback
    @Test
    void should_FindVehicleById_When_VehicleExists(){
        VehicleEntity entitySaved = VehicleEntity.builder()
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC-1234")
                .type(1)
                .build();
        this.repository.save(entitySaved);

        Optional<VehicleEntity> entityAcual = this.repository.findById(entitySaved.getId());
        assertTrue(entityAcual.isPresent());
    }
}
