package edu.java.bot.exceptions;

import java.util.List;
import java.util.stream.Collectors;

public class ChatNotFoundException extends ApiBadRequestException {

    public ChatNotFoundException(List<Long> ids) {
        super(
            "ChatNotFoundException",
            "Следующие Id чатов не были найдены: "
                + ids.stream().map(String::valueOf).collect(Collectors.joining(", "))
        );
    }
}
