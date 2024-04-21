package com.api.parkingcontrol.controller.vehicle;

import com.api.parkingcontrol.builder.dto.vehicle.VehicleDtoBuilder;
import com.api.parkingcontrol.builder.entity.vehicle.VehicleEntityBuilder;
import com.api.parkingcontrol.exception.details.ExceptionDetails;
import com.api.parkingcontrol.exception.details.FieldErrorsExceptionDetails;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import com.api.parkingcontrol.repository.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleControllerITCase {

    private static HttpHeaders DEFAULT_HEADERS;

    @SpyBean
    private VehicleRepository spyRepository;

    private final TestRestTemplate restTemplate;
    private final VehicleDtoBuilder dtoBuilder;
    private final VehicleEntityBuilder entityBuilder;

    @Autowired
    public VehicleControllerITCase(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.dtoBuilder = new VehicleDtoBuilder();
        this.entityBuilder = new VehicleEntityBuilder();
    }

    @BeforeAll
    static void setUp(){
        DEFAULT_HEADERS = new HttpHeaders();
        DEFAULT_HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @Nested
    class PostEndpointScenarios {
        @Test
        void should_Return500AndExceptionDetails_When_NotSaveVehicle() {
            doThrow(DataIntegrityViolationException.class).when(spyRepository).save(any(VehicleEntity.class));

            ResponseEntity<ExceptionDetails> response = restTemplate.postForEntity("/vehicle", dtoBuilder.getCarPostDto(),
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
            defaultExceptionDetailsValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void should_Return400AndExceptionDetails_When_IdIsNotNull() {
            ResponseEntity<ExceptionDetails> response = restTemplate.postForEntity("/vehicle", dtoBuilder.getCarDto(UUID.randomUUID()),
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.BAD_REQUEST);
            defaultExceptionDetailsValidation(response, HttpStatus.BAD_REQUEST);
        }

        @Test
        void should_Return400AndExceptionDetails_When_VehicleIsInvalid() {

            VehicleDto postDto = dtoBuilder.getCarPostDto();
            postDto.setColor(null);
            postDto.setPlate("ABC12345");
            postDto.setType(0);

            ResponseEntity<FieldErrorsExceptionDetails> response = restTemplate.postForEntity("/vehicle", postDto,
                    FieldErrorsExceptionDetails.class);

            defaultValidation(response, HttpStatus.BAD_REQUEST);

            assertThat(response.getBody().getTitle()).isNotNull().isNotEmpty();
            assertThat(response.getBody().getDetails()).isNotNull().isNotEmpty();
            assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.getBody().getFieldError()).isNotNull().isNotEmpty().hasSize(3);
        }
    }

    @Nested
    class GetEndpointScenarios {
        @Test
        void should_Return404AndExceptionDetails_When_NotFoundVehicle() {
            doReturn(Optional.empty()).when(spyRepository).findById(any(UUID.class));

            ResponseEntity<ExceptionDetails> response = restTemplate.getForEntity("/vehicle/" + UUID.randomUUID(),
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.NOT_FOUND);
            defaultExceptionDetailsValidation(response, HttpStatus.NOT_FOUND);
        }

        @ParameterizedTest
        @ValueSource(strings = {"null", "12345"})
        void should_Return400AndExceptionDetails_When_IdIsInvalid(String invalidID) {
            ResponseEntity<ExceptionDetails> response = restTemplate.getForEntity("/vehicle/" + invalidID,
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.BAD_REQUEST);
            defaultExceptionDetailsValidation(response, HttpStatus.BAD_REQUEST);

        }

        @Test
        void should_Return500AndExceptionDetails_When_ErrorOccursDuringOperation() {
            doThrow(DataRetrievalFailureException.class).when(spyRepository).findById(any(UUID.class));

            ResponseEntity<ExceptionDetails> response = restTemplate.getForEntity("/vehicle/" + UUID.randomUUID(),
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
            defaultExceptionDetailsValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class PutEndpointScenarios {
        @Test
        void should_Return505AndExceptionDetails_When_FindButNotUpdateVehicle(){
            doReturn(Optional.of(entityBuilder.getCarEntity(UUID.randomUUID()))).when(spyRepository).findById(any(UUID.class));
            doThrow(DataIntegrityViolationException.class).when(spyRepository).save(any(VehicleEntity.class));

            HttpEntity<VehicleDto> requestEntity = new HttpEntity<>(dtoBuilder.getCarDto(UUID.randomUUID()), DEFAULT_HEADERS);

            ResponseEntity<ExceptionDetails> response = restTemplate.exchange("/vehicle", HttpMethod.PUT, requestEntity,
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
            defaultExceptionDetailsValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void should_Return404AndExceptionDetails_When_NotFindVehicleInUpdateOperation(){
            doReturn(Optional.empty()).when(spyRepository).findById(any(UUID.class));

            HttpEntity<VehicleDto> requestEntity = new HttpEntity<>(dtoBuilder.getCarDto(UUID.randomUUID()), DEFAULT_HEADERS);

            ResponseEntity<ExceptionDetails> response = restTemplate.exchange("/vehicle", HttpMethod.PUT, requestEntity,
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.NOT_FOUND);
            defaultExceptionDetailsValidation(response, HttpStatus.NOT_FOUND);
        }

        @Test
        void should_Return400AndExceptionDetails_When_IdIsNull(){
            HttpEntity<VehicleDto> requestEntity = new HttpEntity<>(dtoBuilder.getCarPostDto(), DEFAULT_HEADERS);

            ResponseEntity<ExceptionDetails> response = restTemplate.exchange("/vehicle", HttpMethod.PUT, requestEntity,
                    ExceptionDetails.class);

            defaultValidation(response, HttpStatus.BAD_REQUEST);
            defaultExceptionDetailsValidation(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class DeleteEndpointScenarios {
        @Test
        void should_Return404AndExceptionDetails_When_NotFindVehicleInDeleteOperation() {
            doReturn(Optional.empty()).when(spyRepository).findById(any(UUID.class));

            ResponseEntity<ExceptionDetails> response = restTemplate.exchange("/vehicle/{id}", HttpMethod.DELETE,
                    null, ExceptionDetails.class, UUID.randomUUID());

            defaultValidation(response, HttpStatus.NOT_FOUND);
            defaultExceptionDetailsValidation(response, HttpStatus.NOT_FOUND);
        }

        @Test
        void should_Return500AndExceptionDetails_When_FindButNotDeleteVehicle() {
            doReturn(Optional.of(entityBuilder.getCarEntity(UUID.randomUUID()))).when(spyRepository).findById(any(UUID.class));
            doThrow(DataIntegrityViolationException.class).when(spyRepository).delete(any(VehicleEntity.class));

            ResponseEntity<ExceptionDetails> response = restTemplate.exchange("/vehicle/{id}", HttpMethod.DELETE,
                    null, ExceptionDetails.class, UUID.randomUUID());

            defaultValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
            defaultExceptionDetailsValidation(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void defaultValidation(ResponseEntity<?> response, HttpStatus status) {
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody()).isNotNull();

    }

    private void defaultExceptionDetailsValidation(ResponseEntity<ExceptionDetails> response, HttpStatus status){
        Objects.requireNonNull(response.getBody());

        assertThat(response.getBody().getTitle()).isNotNull().isNotEmpty();
        assertThat(response.getBody().getDetails()).isNotNull().isNotEmpty();
        assertThat(response.getBody().getStatus()).isEqualTo(status.value());
    }
}
