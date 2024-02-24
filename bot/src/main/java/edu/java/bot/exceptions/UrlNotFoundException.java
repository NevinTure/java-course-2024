package edu.java.bot.exceptions;

import edu.java.bot.model.TgChat;

public class UrlNotFoundException extends BotApiException {

    public UrlNotFoundException(String url, TgChat tgChat) {
        super(
            String.format("Чат с Id %d не отслеживает ссылки: %s", tgChat.getId(), url),
            "UrlNotFoundException"
        );
    }
}
