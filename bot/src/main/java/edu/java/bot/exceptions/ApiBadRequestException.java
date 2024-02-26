package edu.java.bot.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiBadRequestException extends RuntimeException {

    private final String exceptionName;
    private final String description;
}
