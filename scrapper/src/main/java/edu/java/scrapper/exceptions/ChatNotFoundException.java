package edu.java.scrapper.exceptions;

import edu.java.exceptions.ApiNotFoundException;

public class ChatNotFoundException extends ApiNotFoundException {
    public ChatNotFoundException(long id) {
        super(
            String.format("Чат с id %d не найден", id),
            "ChatNotFoundException"
        );
    }
}
