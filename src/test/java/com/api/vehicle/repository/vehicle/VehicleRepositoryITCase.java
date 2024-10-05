package com.api.vehicle.repository.vehicle;

import com.api.vehicle.model.entity.vehicle.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Transactional
    @Rollback
    @Test
    void given_VehiclesExistInDatabase_when_FindVehicle_then_ReturnVehiclePageWithFirstAndSecondSavedVehicle(){
        VehicleEntity firstEntitySaved = VehicleEntity.builder()
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC1234")
                .type(1)
                .build();

        VehicleEntity secondEntitySaved = VehicleEntity.builder()
                .brand("Chevrolet")
                .model("Camaro")
                .color("Amarelo")
                .plate("DEF6789")
                .type(1)
                .build();

        VehicleEntity thirdEntitySaved = VehicleEntity.builder()
                .brand("Honda")
                .model("PCX")
                .color("Preto")
                .plate("YTH1234")
                .type(2)
                .build();

        this.repository.save(firstEntitySaved);
        this.repository.save(secondEntitySaved);
        this.repository.save(thirdEntitySaved);

        Page<VehicleEntity> actual = this.repository.findAll(PageRequest.of(0, 2));

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalElements()).isEqualTo(3);
        assertThat(actual.getNumberOfElements()).isEqualTo(2);
        assertThat(actual.getContent()).containsExactlyInAnyOrder(firstEntitySaved, secondEntitySaved);
    }

    @Transactional
    @Rollback
    @Test
    void given_VehiclesWithDifferentTypesExistInDatabase_when_SearchVehicleByTypes_then_ReturnVehiclePageWithSpecificType(){
        VehicleEntity firstEntitySaved = VehicleEntity.builder()
                .brand("Ford")
                .model("Fiesta")
                .color("Prata")
                .plate("ABC1234")
                .type(1)
                .build();

        VehicleEntity secondEntitySaved = VehicleEntity.builder()
                .brand("Honda")
                .model("Honda ADV")
                .color("Prata")
                .plate("DEF6789")
                .type(2)
                .build();

        VehicleEntity thirdEntitySaved = VehicleEntity.builder()
                .brand("Honda")
                .model("PCX")
                .color("Preto")
                .plate("YTH1234")
                .type(2)
                .build();

        this.repository.save(firstEntitySaved);
        this.repository.save(secondEntitySaved);
        this.repository.save(thirdEntitySaved);

        Page<VehicleEntity> actual = this.repository.findAllByType(2, PageRequest.of(0, 2));

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalElements()).isEqualTo(2);
        assertThat(actual.getNumberOfElements()).isEqualTo(2);
        assertThat(actual.getContent()).containsExactlyInAnyOrder(secondEntitySaved, thirdEntitySaved);
    }
}
