package com.api.vehicle.controller.vehicle;

import com.api.vehicle.builder.dto.vehicle.VehicleDtoBuilder;
import com.api.vehicle.builder.entity.vehicle.VehicleEntityBuilder;
import com.api.vehicle.exception.details.ExceptionDetails;
import com.api.vehicle.exception.details.FieldErrorsExceptionDetails;
import com.api.vehicle.model.dto.page.ResponsePageDto;
import com.api.vehicle.model.dto.VehicleDto;
import com.api.vehicle.model.entity.VehicleEntity;
import com.api.vehicle.repository.VehicleRepository;
import jakarta.persistence.Table;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
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

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VehicleControllerITCase(TestRestTemplate restTemplate, JdbcTemplate jdbcTemplate) {
        this.restTemplate = restTemplate;
        this.dtoBuilder = new VehicleDtoBuilder();
        this.entityBuilder = new VehicleEntityBuilder();
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeAll
    static void setUp(){
        DEFAULT_HEADERS = new HttpHeaders();
        DEFAULT_HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM "+VehicleEntity.class.getAnnotation(Table.class).name());
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

        @Test
        void should_Return200_When_VehicleIsValid(){
            ResponseEntity<VehicleDto> response = restTemplate.postForEntity("/vehicle", dtoBuilder.getCarPostDto(),
                    VehicleDto.class);
            defaultValidation(response, HttpStatus.OK);
        }

        @Test
        void should_ReturnSavedVehicleWithId_When_VehicleIsValid(){
            VehicleDto vehicleExpected = dtoBuilder.getCarPostDto();

            ResponseEntity<VehicleDto> response = restTemplate.postForEntity("/vehicle", vehicleExpected, VehicleDto.class);

            VehicleDto vehicleActual = response.getBody();
            assertThat(vehicleActual).isNotNull();
            assertThat(vehicleActual.getId()).isNotNull();
            assertThat(vehicleActual).usingRecursiveComparison().ignoringFields("id").isEqualTo(vehicleExpected);

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

        @Test
        void should_Return200AndFindedVehicle_When_FindVehicle(){
            VehicleDto postDto = dtoBuilder.getCarPostDto();

            ResponseEntity<VehicleDto> postResponse = restTemplate.postForEntity("/vehicle", postDto, VehicleDto.class);

            VehicleDto vehicleExpected = postResponse.getBody();
            assertThat(vehicleExpected).isNotNull();

            ResponseEntity<VehicleDto> getResponse = restTemplate.getForEntity("/vehicle/" + vehicleExpected.getId(),
                    VehicleDto.class);

            VehicleDto vehicleActual = getResponse.getBody();

            defaultValidation(getResponse, HttpStatus.OK);
            assertThat(vehicleActual).isNotNull().usingRecursiveComparison().isEqualTo(vehicleExpected);
        }

        @ParameterizedTest
        @CsvSource({
                "0, -1",
                "-1, 2",
                "-1, -1"
        })
        void given_InvalidPageNumberOrPageSize_when_ListVehicle_then_Return400AndExceptionDetails(int pageNumber, int pageSize){
            String url = "/vehicle/findAll?pageNumber={pageNumber}&pageSize={pageSize}";
            ResponseEntity<ExceptionDetails> response = restTemplate.getForEntity(url, ExceptionDetails.class, pageNumber, pageSize);

            defaultValidation(response, HttpStatus.BAD_REQUEST);
            defaultExceptionDetailsValidation(response, HttpStatus.BAD_REQUEST);
        }

        @Test
        void given_ValidPageNumberOrPageSize_when_ListVehicle_then_Return200AndVehiclePage(){

            VehicleDto firstResponse = restTemplate.postForEntity("/vehicle", dtoBuilder.getCarPostDto(), VehicleDto.class).getBody();
            VehicleDto secondResponse = restTemplate.postForEntity("/vehicle", dtoBuilder.getCarPostDto(), VehicleDto.class).getBody();
            restTemplate.postForEntity("/vehicle", dtoBuilder.getCarPostDto(), VehicleDto.class).getBody();

            ResponsePageDto<VehicleDto> expected =  ResponsePageDto.<VehicleDto>builder()
                    .totalPages(2)
                    .totalItems(3)
                    .pageSize(2)
                    .currentPage(0)
                    .listContent(List.of(firstResponse, secondResponse)).build();

            String url = "/vehicle/findAll?pageNumber={pageNumber}&pageSize={pageSize}";
            ParameterizedTypeReference<ResponsePageDto<VehicleDto>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ResponsePageDto<VehicleDto>> actual = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    responseType,
                    0,
                    2
            );

            defaultValidation(actual, HttpStatus.OK);
            assertThat(actual.getBody()).usingRecursiveComparison().isEqualTo(expected);
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

        @Test
        void should_Return200AndUpdatedVehicle_When_FindVehicle(){
            //Save vehicle
            VehicleDto postDto = dtoBuilder.getCarPostDto();

            ResponseEntity<VehicleDto> postResponse = restTemplate.postForEntity("/vehicle", postDto, VehicleDto.class);

            VehicleDto expectedVehicle = postResponse.getBody();
            assertThat(expectedVehicle).isNotNull();

            //Update vehicle
            expectedVehicle.setColor("Black");

            HttpEntity<VehicleDto> requestEntity = new HttpEntity<>(expectedVehicle, DEFAULT_HEADERS);

            ResponseEntity<VehicleDto> putResponse = restTemplate.exchange("/vehicle", HttpMethod.PUT, requestEntity,
                    VehicleDto.class);

            VehicleDto vehicleActual = putResponse.getBody();

            defaultValidation(putResponse, HttpStatus.OK);
            assertThat(vehicleActual).isNotNull().usingRecursiveComparison().isEqualTo(expectedVehicle);
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

        @Test
        void should_Return204AndVehicleNotFoundAfterDeleting_When_DeleteVehicle(){
            //Save vehicle
            VehicleDto postDto = dtoBuilder.getCarPostDto();

            ResponseEntity<VehicleDto> postResponse = restTemplate.postForEntity("/vehicle", postDto, VehicleDto.class);

            VehicleDto savedVehicle = postResponse.getBody();
            assertThat(savedVehicle).isNotNull();

            //Delete vehicle
            ResponseEntity<Void> response = restTemplate.exchange("/vehicle/{id}", HttpMethod.DELETE,
                    null, Void.class, savedVehicle.getId());

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            //Find vehicle
            ResponseEntity<VehicleDto> getResponse = restTemplate.getForEntity("/vehicle/" + savedVehicle.getId(),
                    VehicleDto.class);

            assertThat(getResponse.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_FOUND);
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
