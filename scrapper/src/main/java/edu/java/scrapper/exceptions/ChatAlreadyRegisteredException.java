package edu.java.scrapper.exceptions;

import edu.java.models.exceptions.ApiBadRequestException;

public class ChatAlreadyRegisteredException extends ApiBadRequestException {
    public ChatAlreadyRegisteredException(long id) {
        super(
            String.format("Чат с id %d уже зарегистрирован", id),
            "ChatAlreadyRegisteredException"
        );
    }
}
