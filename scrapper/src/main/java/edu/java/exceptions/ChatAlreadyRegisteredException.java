package edu.java.exceptions;

public class ChatAlreadyRegisteredException extends ApiBadRequestException {
    public ChatAlreadyRegisteredException(long id) {
        super(
            String.format("Чат с id %d уже зарегистрирован", id),
            "ChatAlreadyRegisteredException"
        );
    }
}
