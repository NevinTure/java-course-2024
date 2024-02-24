package edu.java.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ApiBadRequestException extends RuntimeException {

    private final String description;
    private final String name;
}
