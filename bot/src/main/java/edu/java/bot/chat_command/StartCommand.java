package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartCommand implements ChatCommand {

    private final static String SUCCESSFUL_REGISTRATION = """
        Регистрация прошла успешно.
        """;

    @Override
    public SendMessage getMessage(Person person, ChatService service) {
        log.info(String.format("Пользователей с chatId %d был зарегистрирован.", person.getId()));
        return new SendMessage(person.getId(), SUCCESSFUL_REGISTRATION);
    }
}
