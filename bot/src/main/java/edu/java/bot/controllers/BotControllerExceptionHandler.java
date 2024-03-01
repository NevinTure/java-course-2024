package edu.java.bot.controllers;

import edu.java.models.dtos.ApiErrorResponse;
import edu.java.models.exceptions.ApiBadRequestException;
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
public class BotControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiBadRequestException.class)
    public ResponseEntity<Object> handleApiBadRequest(
        ApiBadRequestException e, WebRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getDescription(),
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            e.getExceptionName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        List<String> violatedField = getViolatedFieldNames(ex);
        ApiErrorResponse response = new ApiErrorResponse(
            "Некорректные значения параметров запроса: " + String.join(", ", violatedField),
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            "MethodArgumentNotValidException",
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private List<String> getViolatedFieldNames(MethodArgumentNotValidException ex) {
        return ex
            .getBindingResult()
            .getAllErrors()
            .stream()
            .map(v -> ((FieldError) v).getField())
            .toList();
    }
}
