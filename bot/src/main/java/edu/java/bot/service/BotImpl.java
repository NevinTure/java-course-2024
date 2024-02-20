package edu.java.bot.service;

import com.google.gson.stream.JsonToken;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.UnknownCommand;
import edu.java.bot.model.Person;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BotImpl implements Bot {

    private final TelegramBot telegramBot;
    private final ChatService chatService;
    private final List<ChatCommand> commands;
    private final ChatCommand startCommand;

    public BotImpl(TelegramBot telegramBot,
        ChatService chatService,
        List<ChatCommand> commands,
        ChatCommand startCommand
    ) {
        this.telegramBot = telegramBot;
        this.chatService = chatService;
        this.commands = commands;
        this.startCommand = startCommand;
        start();
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            Message message = update.message();
            String text = message.text().trim();
            long id = message.chat().id();
            Optional<Person> optionalPerson = chatService.getById(id);
            ChatCommand commandToPerform = new UnknownCommand();
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                for (ChatCommand command : commands) {
                    if (command.handle(text, person)) {
                        commandToPerform = command;
                        break;
                    }
                }
                chatService.save(person);
            } else {
                if (startCommand.handle(text, null)) {
                    commandToPerform = startCommand;
                    chatService.save(new Person(id));
                }
            }
            SendMessage request = commandToPerform.getMessage(id);
            execute(request);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        SetMyCommands menuCommands = new SetMyCommands(menuCommandArray());
        execute(menuCommands);
        telegramBot.setUpdatesListener(
            this,
            e -> {
                if (e.response() != null) {
                    e.response().errorCode();
                    e.response().description();
                } else {
                    log.error(e.toString());
                }
            }
        );
    }

    public BotCommand[] menuCommandArray() {
        return new BotCommand[] {
            new BotCommand("/start", "зарегистрировать пользователя"),
            new BotCommand("/help", "вывести окно с командами"),
            new BotCommand("/track", "начать отслеживание ссылки"),
            new BotCommand("/untrack", "прекратить отслеживание ссылки"),
            new BotCommand("/list", "показать список отслеживаемых ссылок")
        };
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
