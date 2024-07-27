package com.api.parkingcontrol.mapper.page;

import com.api.parkingcontrol.model.dto.vehicle.page.ResponseVehiclePageDto;
import com.api.parkingcontrol.model.entity.vehicle.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface VehiclePageMapper {

    @Mapping(source = "content", target = "listVehicleDto")
    @Mapping(source = "number", target = "currentPage")
    @Mapping(source = "size", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalItems")
    ResponseVehiclePageDto pageEntityToPageDto(Page<VehicleEntity> pageEntity);
}
