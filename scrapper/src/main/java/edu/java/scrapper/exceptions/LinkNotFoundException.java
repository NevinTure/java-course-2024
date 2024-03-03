package edu.java.scrapper.exceptions;

import edu.java.models.exceptions.ApiNotFoundException;
import java.net.URI;

public class LinkNotFoundException extends ApiNotFoundException {
    public LinkNotFoundException(long id, URI url) {
        super(
            String.format("Чат с id %d не отслеживает ссылку %s", id, url),
            "LinkNotFoundException"
        );
    }
}
