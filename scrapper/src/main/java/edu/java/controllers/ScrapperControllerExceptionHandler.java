package edu.java.controllers;

import edu.java.dtos.ApiErrorResponse;
import edu.java.exceptions.ApiBadRequestException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ScrapperControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiBadRequestException.class)
    public ResponseEntity<Object> handleApiBadRequest(
        ApiBadRequestException e, WebRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getDescription(),
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            e.getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(String::valueOf).toArray(String[]::new)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
