package edu.java.scrapper.exceptions;

import edu.java.models.exceptions.ApiBadRequestException;
import java.net.URI;

public class LinkAlreadyTrackedException extends ApiBadRequestException {
    public LinkAlreadyTrackedException(long id, URI url) {
        super(
            String.format("Чат с id %d уже отслеживает ссылку %s", id, url),
            "LinkAlreadyTrackedException"
        );
    }
}
