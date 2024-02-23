package edu.java.bot.exceptions;

import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

public class ChatNotFoundException extends BotApiException {

    public ChatNotFoundException(List<Long> ids) {
        setDescription("Следующие Id чатов не были найдены: "
            + ids.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        setName("ChatNotFoundException");
    }
}
