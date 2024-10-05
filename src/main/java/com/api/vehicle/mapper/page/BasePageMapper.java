package com.api.vehicle.mapper.page;

import com.api.vehicle.model.dto.page.ResponsePageDto;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

public interface BasePageMapper<U, T>{

    @Mapping(source = "number", target = "currentPage")
    @Mapping(source = "size", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalItems")
    @Mapping(source = "content", target = "listContent")
    ResponsePageDto<U> pageEntityToPageDto(Page<T> pageEntity);
}
