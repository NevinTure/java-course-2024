package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.ListCommand;

public class ListCommandHandler extends CommandHandler {

    @Override
    public ChatCommand handle(String text) {
        if (text.equals("/list")) {
            return new ListCommand();
        }
        return getNext().handle(text);
    }
}
