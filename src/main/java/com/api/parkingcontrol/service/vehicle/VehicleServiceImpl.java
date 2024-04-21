package com.api.parkingcontrol.service.vehicle;

import com.api.parkingcontrol.exception.response.BadRequestException;
import com.api.parkingcontrol.exception.response.InternalServerErrorException;
import com.api.parkingcontrol.exception.response.NotFoundException;
import com.api.parkingcontrol.mapper.vehicle.VehicleMapper;
import com.api.parkingcontrol.model.dto.vehicle.VehicleDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import com.api.parkingcontrol.repository.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository repository;
    private final VehicleMapper mapper;

    @Autowired
    public VehicleServiceImpl(VehicleRepository repository, VehicleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public VehicleDto save(VehicleDto vehicleDto) {
        try{

            if(Objects.nonNull(vehicleDto.getId())){
                throw new BadRequestException("ID must be null to save a vehicle! Use the PUT endpoint if you want to" +
                        " update an existing vehicle.");
            }

            VehicleEntity savedVehicle = this.repository.save(this.mapper.dtoToEntity(vehicleDto));
            return this.mapper.entityToDto(savedVehicle);
        }catch (DataAccessException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void delete(UUID id) {
        try{
            VehicleDto vehicleDto = this.findById(id);
            this.repository.delete(this.mapper.dtoToEntity(vehicleDto));
        }catch (DataAccessException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public VehicleDto findById(UUID id) {
        try{
            if(Objects.isNull(id)){
                throw new BadRequestException("Id can't be null!");
            }

            Optional<VehicleEntity> optVehicle = this.repository.findById(id);

            if (optVehicle.isEmpty()) {
                throw new NotFoundException("Vehicle not found!");
            }
            return this.mapper.entityToDto(optVehicle.get());
        }catch (DataAccessException e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public VehicleDto update(VehicleDto dto) {
        try{
            VehicleDto foundVehicle = findById(dto.getId());
            VehicleEntity updatedVehicle = this.repository.save(this.mapper.dtoToEntity(foundVehicle));

            return this.mapper.entityToDto(updatedVehicle);
        }catch (DataAccessException e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}