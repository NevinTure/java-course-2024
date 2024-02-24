package edu.java.bot.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BotApiException extends RuntimeException {

    private final String name;
    private final String description;
}
