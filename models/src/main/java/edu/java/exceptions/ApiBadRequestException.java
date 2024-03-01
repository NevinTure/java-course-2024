package edu.java.exceptions;

public class ApiBadRequestException extends ApiException {

    public ApiBadRequestException(String description, String name) {
        super(description, name);
    }
}
