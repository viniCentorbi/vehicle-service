package com.api.parkingcontrol.exception.handler;

import com.api.parkingcontrol.exception.details.ExceptionDetails;
import com.api.parkingcontrol.exception.details.FieldErrorsExceptionDetails;
import com.api.parkingcontrol.exception.response.BadRequestException;
import com.api.parkingcontrol.exception.response.InternalServerErrorException;
import com.api.parkingcontrol.exception.response.NotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class ParkingControlExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_DETAIL = "The operation could not be performed.";

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionDetails> handlerInternalServerErrorException(InternalServerErrorException e){
        return new ResponseEntity<>(ExceptionDetails.builder()
                .title("Internal Server Error Exception")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .details(Objects.isNull(e.getMessage()) ? DEFAULT_DETAIL : e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDetails> handlerBadRequestException(BadRequestException e){
        return new ResponseEntity<>(ExceptionDetails.builder()
                .title("Bad Request Exception")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(Objects.isNull(e.getMessage()) ? DEFAULT_DETAIL : e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDetails> handlerNotFoundException(NotFoundException e){
        return new ResponseEntity<>(ExceptionDetails.builder()
                .title("Not Found Exception")
                .status(HttpStatus.NOT_FOUND.value())
                .details(Objects.isNull(e.getMessage()) ? DEFAULT_DETAIL : e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException e, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        ExceptionDetails details = ExceptionDetails.builder()
                .title("Type Mismatch Exception")
                .status(status.value())
                .details(Objects.isNull(e.getMessage()) ? DEFAULT_DETAIL : e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(e, details, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        Map<String, String> fieldErrorMap = new HashMap<>();

        fieldErrorList.forEach(error -> fieldErrorMap.put(error.getField(), error.getDefaultMessage()));

        FieldErrorsExceptionDetails details = FieldErrorsExceptionDetails.builder()
                .title("Method Argument Not Valid Exception")
                .status(status.value())
                .details("Invalid Fields! Please check the field(s) error.")
                .fieldError(fieldErrorMap)
                .timestamp(LocalDateTime.now())
                .build();

        return this.handleExceptionInternal(e, details, headers, status, request);
    }
}
