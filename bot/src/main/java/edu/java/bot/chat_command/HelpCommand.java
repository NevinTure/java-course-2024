package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.TgChat;
import edu.java.models.utils.State;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements ChatCommand {

    private final String helpOutput;

    @Autowired
    public HelpCommand(List<ChatCommand> commandList) {
        commandList.add(this);
        helpOutput = commandList
            .stream()
            .map(ChatCommand::getDescription)
            .filter(v -> !v.isBlank())
            .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean checkState(State state) {
        return state.equals(State.DEFAULT);
    }

    @Override
    public boolean handle(String text, TgChat sender) {
        return Objects.equals(text, "/help");
    }

    @Override
    public SendMessage getMessage(long receiverId) {
        return new SendMessage(receiverId, helpOutput);
    }

    @Override
    public String getDescription() {
        return "/help -- вывести окно с командами";
    }
}
