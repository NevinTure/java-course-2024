package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import lombok.extern.java.Log;

@Log
public class UnknownCommand implements ChatCommand {

    private static final String UNKNOWN_COMMAND = """
        Неизвестная команда.
        """;

    @Override
    public SendMessage getMessage(Person person, ChatService service) {
        log.info(String.format("Пользователь %d отправил неизвестную команду", person.getId()));
        return new SendMessage(person.getId(), UNKNOWN_COMMAND);
    }
}
