package com.api.vehicle.exception.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetails {

    protected String title;
    protected int status;
    protected String details;
    protected LocalDateTime timestamp;
}
