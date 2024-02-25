package edu.java.exceptions;

import java.net.URI;

public class LinkNotFoundException extends ApiNotFoundException {
    public LinkNotFoundException(long id, URI url) {
        super(
            String.format("Чат с id %d не отслеживает ссылку %s", id, url),
            "LinkNotFoundException"
        );
    }
}
