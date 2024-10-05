package com.api.vehicle.controller;

import com.api.vehicle.enums.type.EnumVehicleType;
import com.api.vehicle.model.dto.page.ResponsePageDto;
import com.api.vehicle.model.dto.vehicle.VehicleDto;
import com.api.vehicle.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("vehicle")
public class VehicleController {

    private final VehicleService service;

    @Autowired
    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> save(@RequestBody @Valid VehicleDto postDto){
        return ResponseEntity.ok(this.service.save(postDto));
    }

    @PutMapping
    public ResponseEntity<VehicleDto> update(@RequestBody @Valid VehicleDto putDto) {
        return ResponseEntity.ok(this.service.update(putDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<ResponsePageDto<VehicleDto>> findAll( @RequestParam(defaultValue = "0") int pageNumber,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(this.service.findAll(pageNumber, pageSize));
    }

    @GetMapping("/findAllByType")
    public ResponseEntity<ResponsePageDto<VehicleDto>> findAllByType(@RequestParam EnumVehicleType type,
                                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(this.service.findAllByType(type, pageNumber, pageSize));
    }
}
