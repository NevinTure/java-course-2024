package edu.java.models.exceptions;

public class ApiNotFoundException extends ApiException {
    public ApiNotFoundException(String description, String name) {
        super(description, name);
    }
}
