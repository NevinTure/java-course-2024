package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.TgChat;
import edu.java.models.utils.State;

public interface ChatCommand {

    boolean checkState(State state);

    boolean handle(String text, TgChat sender);

    SendMessage getMessage(long receiverId);

    String getDescription();
}
