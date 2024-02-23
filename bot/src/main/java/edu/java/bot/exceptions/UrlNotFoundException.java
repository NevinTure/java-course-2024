package edu.java.bot.exceptions;

import edu.java.bot.model.Person;

public class UrlNotFoundException extends BotApiException {

    public UrlNotFoundException(String url, Person person) {
        setDescription(String.format("Чат с Id %d не отслеживает ссылки: %s", person.getId(), url));
        setName("UrlNotFoundException");
    }
}
