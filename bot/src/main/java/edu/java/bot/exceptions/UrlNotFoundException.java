package edu.java.bot.exceptions;

import edu.java.bot.model.TgChat;

public class UrlNotFoundException extends ApiBadRequestException {

    public UrlNotFoundException(String url, TgChat tgChat) {
        super(
            "UrlNotFoundException",
            String.format("Чат с Id %d не отслеживает ссылки: %s", tgChat.getId(), url)
        );
    }
}
