package edu.java.bot.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BotApiException extends RuntimeException {

    private String name = "BotApiException";
    private String description;
}
