package edu.java.bot.exceptions;

import edu.java.bot.model.TgChat;
import edu.java.models.exceptions.ApiBadRequestException;
import java.net.URI;

public class UrlNotFoundException extends ApiBadRequestException {

    public UrlNotFoundException(URI url, TgChat tgChat) {
        super(
            String.format("Чат с Id %d не отслеживает ссылки: %s", tgChat.getId(), url),
            "UrlNotFoundException"
        );
    }
}
