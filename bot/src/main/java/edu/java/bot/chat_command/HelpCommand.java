package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import lombok.extern.java.Log;

@Log
public class HelpCommand implements ChatCommand {

    private static final String COMMAND_LIST = """
        Список команд:
        /start -- зарегистрировать пользователя
        /help -- вывести окно с командами
        /track -- начать отслеживание ссылки
        /untrack -- прекратить отслеживание ссылки
        /list -- показать список отслеживаемых ссылок
        """;

    @Override
    public SendMessage getMessage(Person person, ChatService service) {
        log.info("Вывели список команд");
        return new SendMessage(person.getId(), COMMAND_LIST);
    }
}
