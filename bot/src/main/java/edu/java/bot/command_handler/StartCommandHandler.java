package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.StartCommand;

public class StartCommandHandler extends CommandHandler {
    @Override
    public ChatCommand handle(String text) {
        if (text.equals("/start")) {
            return new StartCommand();
        }
        return getNext().handle(text);
    }
}
