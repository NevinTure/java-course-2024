package edu.java.controllers;

import edu.java.dtos.ApiErrorResponse;
import edu.java.exceptions.ApiBadRequestException;
import edu.java.exceptions.ApiNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ScrapperControllerExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<Object> handleApiNotFound(
        ApiNotFoundException e, WebRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getDescription(),
            String.valueOf(HttpStatus.NOT_FOUND.value()),
            e.getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(String::valueOf).toArray(String[]::new)
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        List<String> violatedField = ex
            .getBindingResult()
            .getAllErrors()
            .stream()
            .map(v -> ((FieldError) v).getField())
            .toList();
        ApiErrorResponse response = new ApiErrorResponse(
            "Некорректные значения параметров запроса: " + String.join(", ", violatedField),
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            "MethodArgumentNotValidException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
