package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.HelpCommand;

public class HelpCommandHandler extends CommandHandler {

    @Override
    public ChatCommand handle(String text) {
        if (text.equals("/help")) {
            return new HelpCommand();
        }
        return getNext().handle(text);
    }
}
