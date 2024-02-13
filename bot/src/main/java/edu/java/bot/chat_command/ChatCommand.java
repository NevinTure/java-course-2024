package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;

public interface ChatCommand {

    SendMessage getMessage(Person person, ChatService service);
}
