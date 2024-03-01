package edu.java.exceptions;

public class ApiNotFoundException extends ApiException {
    public ApiNotFoundException(String description, String name) {
        super(description, name);
    }
}
