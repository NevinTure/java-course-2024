package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.State;
import edu.java.bot.model.TgChat;
import lombok.extern.java.Log;

@Log
public class UnknownCommand implements ChatCommand {

    private static final String MESSAGE = "Неизвестная команда.";

    @Override
    public boolean checkState(State state) {
        return true;
    }

    @Override
    public boolean handle(String text, TgChat sender) {
        return true;
    }

    @Override
    public SendMessage getMessage(long receiverId) {
        return new SendMessage(receiverId, MESSAGE);
    }

    @Override
    public String getDescription() {
        return "un";
    }
}
