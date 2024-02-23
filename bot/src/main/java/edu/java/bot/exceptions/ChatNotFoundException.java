package edu.java.bot.exceptions;

import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatNotFoundException extends RuntimeException {

    private final String description;
    private final String name = "ChatNotFoundException";

    public ChatNotFoundException(List<Long> ids) {
        description = "Following Ids not found: "
            + ids.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }
}
