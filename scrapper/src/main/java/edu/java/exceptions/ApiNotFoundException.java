package edu.java.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class ApiNotFoundException extends ApiException {
    public ApiNotFoundException(String description, String name) {
        super(description, name);
    }
}
