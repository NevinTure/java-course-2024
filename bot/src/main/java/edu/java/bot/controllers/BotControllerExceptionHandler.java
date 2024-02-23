package edu.java.bot.controllers;

import edu.java.bot.dtos.ApiErrorResponse;
import edu.java.bot.exceptions.ChatNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BotControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<Object> handleChatNotFound(
        ChatNotFoundException e, WebRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getDescription(),
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            e.getName(),
            e.getMessage(),
            e.getStackTrace()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
