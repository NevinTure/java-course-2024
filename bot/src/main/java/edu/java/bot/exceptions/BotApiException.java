package edu.java.bot.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class BotApiException extends RuntimeException {

    private final String description;
    private final String code = "";
    private final String name = "Basic Bot api exception";


}
