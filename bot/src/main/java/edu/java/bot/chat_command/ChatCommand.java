package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.TgChat;

public interface ChatCommand {

    boolean handle(String text, TgChat sender);

    SendMessage getMessage(long receiverId);

    String getDescription();
}
