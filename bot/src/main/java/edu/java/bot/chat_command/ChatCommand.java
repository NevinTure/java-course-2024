package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;

public interface ChatCommand {

    boolean handle(String text, Person sender);

    SendMessage getMessage(long receiverId);

    String getDescription();
}
