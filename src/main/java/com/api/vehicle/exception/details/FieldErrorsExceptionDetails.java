package com.api.vehicle.exception.details;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class FieldErrorsExceptionDetails extends ExceptionDetails{
    /**
     * Stores the field name and its respective error message
     */
    private Map<String, String> fieldError;
}
