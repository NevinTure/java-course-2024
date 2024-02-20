package edu.java.bot.service;

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
import edu.java.bot.command_handler.CommandHandler;
import edu.java.bot.model.Person;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BotImpl implements Bot {

    private final TelegramBot telegramBot;
    private final ChatService chatService;
    private final CommandHandler commandHandler;

    public BotImpl(TelegramBot telegramBot, ChatService chatService, CommandHandler commandHandler) {
        this.telegramBot = telegramBot;
        this.chatService = chatService;
        this.commandHandler = commandHandler;
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
            Optional<Person> optionalPerson = chatService.getById(id);
            ChatCommand commandToPerform;
            if (optionalPerson.isPresent()) {
                for (ChatCommand command : commandList) {
                    if (command.handle(text, optionalPerson.get())) {
                        commandToPerform = command;
                        break;
                    }
                }
                chatService.save(person);
            } else {
                if (startCommand.resolve(text, null)) {
                    commandToPerform = startCommand;
                    chatService.save(new Person(id));
                }
            }
            SendMessage request = commandToPerform.getMessage();
            execute(request);
//            ChatCommand command = commandHandler.handle(text);
//            Person person = new Person(message.chat().id());
//            SendMessage request = command.getMessage(person, chatService);
//            execute(request);
//            Long id = message.chat().id();
//            SendMessage request;
//            Person person;
//            Optional<Person> optionalPerson = chatService.findById(id);
//            if (optionalPerson.isPresent()) {
//                person = optionalPerson.get();
//                if (person.isWaitingTrack()) {
//                    InteractiveCommand interactiveCommand = new TrackInteractiveCommand(text);
//                    request = interactiveCommand.getMessage(person, chatService);
//                } else if (person.isWaitingUntrack()) {
//                    InteractiveCommand interactiveCommand = new UntrackInteractiveCommand(text);
//                    request = interactiveCommand.getMessage(person, chatService);
//                } else {
//                    request = commandHandler.handle(text).getMessage(person);
//                }
//            } else {
//                InteractiveCommand interactiveCommand = startHandler.handle(text);
//                person = new Person(id);
//                request = interactiveCommand.getMessage(person, chatService);
//            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        SetMyCommands commands = new SetMyCommands(menuCommandArray());
        execute(commands);
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
