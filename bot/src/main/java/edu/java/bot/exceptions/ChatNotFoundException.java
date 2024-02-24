package edu.java.bot.exceptions;

import java.util.List;
import java.util.stream.Collectors;

public class ChatNotFoundException extends BotApiException {

    public ChatNotFoundException(List<Long> ids) {
        super(
            "Следующие Id чатов не были найдены: "
                + ids.stream().map(String::valueOf).collect(Collectors.joining(", ")),
            "ChatNotFoundException"
        );
    }
}
