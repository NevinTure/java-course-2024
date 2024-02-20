package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import lombok.extern.java.Log;
import java.util.List;
import java.util.Objects;

@Log
public class HelpCommand implements ChatCommand {

    private final String commandList;

    public HelpCommand(String commandList) {
        this.commandList = commandList;
    }

    @Override
    public boolean handle(String text, Person sender) {
        return Objects.equals(text, "/help");
    }

    @Override
    public SendMessage getMessage(long receiverId) {
        return new SendMessage(receiverId, commandList);
    }

    @Override
    public String getDescription() {
        return "/help -- вывести окно с командами";
    }
}
