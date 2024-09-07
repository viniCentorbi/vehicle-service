package com.api.parkingcontrol.service.vehicle;

import com.api.parkingcontrol.builder.dto.vehicle.VehicleDtoBuilder;
import com.api.parkingcontrol.builder.entity.vehicle.VehicleEntityBuilder;
import com.api.parkingcontrol.exception.response.BadRequestException;
import com.api.parkingcontrol.exception.response.InternalServerErrorException;
import com.api.parkingcontrol.exception.response.NotFoundException;
import com.api.parkingcontrol.mapper.vehicle.VehiclePageMapper;
import com.api.parkingcontrol.model.dto.page.ResponsePageDto;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import com.api.parkingcontrol.repository.vehicle.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
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
    @MockBean
    private VehiclePageMapper mockVehiclePageMapper;
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
    void should_ThrowsBadRequestException_When_IdIsNotNullInSaveOperation(){
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(UUID.randomUUID());
        assertThrows(BadRequestException.class, () -> this.service.save(dtoExpected));
    }

    @Test
    void should_DontThrowsInternalServerErrorException_When_DeleteVehicle(){
        UUID idDto = UUID.randomUUID();
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idDto);

        doReturn(dtoExpected).when(this.spyService).findById(idDto);
        doNothing().when(this.mockVehicleRepository).delete(any(VehicleEntity.class));

        assertDoesNotThrow(() -> this.service.delete(idDto));
    }

    @Test
    void should_ThrowsInternalServerErrorException_When_NotDeleteVehicle(){
        UUID idDto = UUID.randomUUID();
        VehicleDto dtoExpected = this.dtoBuilder.getCarDto(idDto);

        doReturn(dtoExpected).when(this.spyService).findById(idDto);
        doThrow(OptimisticLockingFailureException.class).when(this.mockVehicleRepository).delete(any(VehicleEntity.class));

        assertThrows(InternalServerErrorException.class, () -> this.service.delete(idDto));
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
    void should_ThrowsInternalServerErrorException_When_CantFindVehicle(){
        UUID id = UUID.randomUUID();
        when(this.mockVehicleRepository.findById(any())).thenThrow(DataRetrievalFailureException.class);
        assertThrows(InternalServerErrorException.class, () -> this.service.findById(id));
    }

    @Test
    void should_ThrowsBadRequestException_When_IdIsNull(){
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

    @Test
    void given_PageNumberAndPageSize_when_ListVehicles_then_ReturnVehiclePage(){
        //Vehicles
        VehicleEntity firstEntity = this.entityBuilder.getCarEntity(UUID.randomUUID());
        VehicleEntity secondEntity = this.entityBuilder.getCarEntity(UUID.randomUUID());

        //Given
        int pageNumber = 0;
        int pageSize = 2;

        //Expected
        ResponsePageDto<VehicleDto> expected = ResponsePageDto.<VehicleDto>builder()
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .totalPages(1)
                .totalItems(4)
                .listContent(List.of(dtoBuilder.getCarDto(firstEntity), dtoBuilder.getCarDto(secondEntity))).build();

        //When
        Page<VehicleEntity> pageEntity = new PageImpl<>(List.of(firstEntity, secondEntity),
                PageRequest.of(pageNumber, pageSize), expected.getTotalItems());

        when(this.mockVehicleRepository.findAll((PageRequest.of(pageNumber, pageSize)))).thenReturn(pageEntity);
        when(this.mockVehiclePageMapper.pageEntityToPageDto(pageEntity)).thenReturn(expected);

        //Then
        ResponsePageDto<VehicleDto> actual = this.service.findAll(pageNumber, pageSize);
        assertThat(actual).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "0, -1",
            "-1, 2",
            "-1, -1"
    })
    void given_InvalidParameters_when_ListVehicles_then_ThrowsBadRequestException(int pageNumber, int pageSize){
        assertThrows(BadRequestException.class,  () -> this.service.findAll(pageNumber, pageSize));
    }

    @Test
    void given_ErrorInDatabase_when_ListVehicles_then_ThrowsInternalServerErrorException(){

        int pageNumber = 0;
        int pageSize = 999999;

        when(this.mockVehicleRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenThrow(QueryTimeoutException.class);
        assertThrows(InternalServerErrorException.class,  () -> this.service.findAll(pageNumber, pageSize));
    }

    @Test
    void given_PageNumberAndPageSizeAndVehicleType_when_ListVehicles_then_ReturnVehiclePageOfSpecifiedType(){
        //Setup
        VehicleEntity firstEntity = this.entityBuilder.getCarEntity(UUID.randomUUID());
        VehicleEntity secondEntity = this.entityBuilder.getCarEntity(UUID.randomUUID());

        //Given
        int pageNumber = 0;
        int pageSize = 2;
        int vehicleType = 1;

        //Expected
        ResponsePageDto<VehicleDto> expected = ResponsePageDto.<VehicleDto>builder()
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .totalPages(1)
                .totalItems(4)
                .listContent(List.of(dtoBuilder.getCarDto(firstEntity), dtoBuilder.getCarDto(secondEntity))).build();


        //When
        Page<VehicleEntity> pageEntity = new PageImpl<>(List.of(firstEntity, secondEntity),
                PageRequest.of(pageNumber, pageSize), expected.getTotalItems());

        when(this.mockVehicleRepository.findAllByType(vehicleType, (PageRequest.of(pageNumber, pageSize)))).thenReturn(pageEntity);
        when(this.mockVehiclePageMapper.pageEntityToPageDto(pageEntity)).thenReturn(expected);

        //Then
        ResponsePageDto<VehicleDto> actual = this.service.findAllByType(vehicleType, pageNumber, pageSize);
        assertThat(actual).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

}
