package edu.java.bot.exceptions;

import edu.java.bot.model.TgChat;
import java.net.URI;

public class UrlNotFoundException extends ApiBadRequestException {

    public UrlNotFoundException(URI url, TgChat tgChat) {
        super(
            "UrlNotFoundException",
            String.format("Чат с Id %d не отслеживает ссылки: %s", tgChat.getId(), url)
        );
    }
}
