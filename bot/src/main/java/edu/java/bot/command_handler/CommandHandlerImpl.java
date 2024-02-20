package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.UnknownCommand;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CommandHandlerImpl implements CommandHandler {

    private final ChatService chatService;
    private final List<ChatCommand> commands;
    private final ChatCommand startCommand;
    private static final ChatCommand UNKNOWN_COMMAND = new UnknownCommand();

    public CommandHandlerImpl(ChatService chatService, List<ChatCommand> commands, ChatCommand startCommand) {
        this.chatService = chatService;
        this.commands = commands;
        this.startCommand = startCommand;
    }

    @Override
    public ChatCommand handle(long senderId, String text) {
        Optional<Person> optionalPerson = chatService.getById(senderId);
        ChatCommand commandToPerform = UNKNOWN_COMMAND;
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
                chatService.save(new Person(senderId));
            }
        }
        return commandToPerform;
    }
}