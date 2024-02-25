package edu.java.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class ApiBadRequestException extends ApiException {

    public ApiBadRequestException(String description, String name) {
        super(description, name);
    }
}
