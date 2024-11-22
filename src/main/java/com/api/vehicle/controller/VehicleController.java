package com.api.vehicle.controller;

import com.api.vehicle.constants.url.Endpoints;
import com.api.vehicle.enums.type.EnumVehicleType;
import com.api.vehicle.exception.details.ExceptionDetails;
import com.api.vehicle.exception.details.FieldErrorsExceptionDetails;
import com.api.vehicle.model.dto.page.ResponsePageDto;
import com.api.vehicle.model.dto.VehicleDto;
import com.api.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping(Endpoints.VEHICLE)
@Tag(name = "vehicle", description = "The vehicle service with documentation annotations")
public class VehicleController {

    private final VehicleService service;

    @Autowired
    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Value", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorsExceptionDetails.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @Operation(summary = "Save vehicle", description = "Save a new vehicle.")
    @PostMapping
    public ResponseEntity<VehicleDto> save(@RequestBody @Valid VehicleDto postDto){
        return ResponseEntity.ok(this.service.save(postDto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Value", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorsExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not found vehicle", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @Operation(summary = "Update vehicle", description = "Update an existing vehicle.")
    @PutMapping
    public ResponseEntity<VehicleDto> update(@RequestBody @Valid VehicleDto putDto) {
        return ResponseEntity.ok(this.service.update(putDto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Value", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorsExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not found vehicle", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @Operation(summary = "Get vehicle", description = "Return a vehicle by id.")
    @GetMapping(Endpoints.PATH_VARIABLE_ID)
    public ResponseEntity<VehicleDto> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle removed"),
            @ApiResponse(responseCode = "400", description = "Invalid Value", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorsExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not found vehicle", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle by id.")
    @DeleteMapping(Endpoints.PATH_VARIABLE_ID)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Value", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorsExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not found vehicle", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @Operation(summary = "Get all vehicles", description = "Return all vehicles.")
    @GetMapping(Endpoints.FIND_ALL_VEHICLES)
    public ResponseEntity<ResponsePageDto<VehicleDto>> findAll( @RequestParam(defaultValue = "0") int pageNumber,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(this.service.findAll(pageNumber, pageSize));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Value", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorsExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not found vehicle", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @Operation(summary = "Get all vehicle by type", description = "Returns all vehicles of a type.")
    @GetMapping(Endpoints.FIND_ALL_VEHICLES_BY_TYPE)
    public ResponseEntity<ResponsePageDto<VehicleDto>> findAllByType(@RequestParam EnumVehicleType type,
                                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(this.service.findAllByType(type, pageNumber, pageSize));
    }
}
