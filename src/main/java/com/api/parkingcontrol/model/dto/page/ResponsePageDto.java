package com.api.parkingcontrol.model.dto.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder()
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageDto<T> {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalItems;
    private List<T> listContent;
}
