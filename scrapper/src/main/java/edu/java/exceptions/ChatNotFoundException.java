package edu.java.exceptions;

public class ChatNotFoundException extends ApiNotFoundException {
    public ChatNotFoundException(long id) {
        super(
            "ChatNotFoundException",
            String.format("Чат с id %d не найден", id)
        );
    }
}
