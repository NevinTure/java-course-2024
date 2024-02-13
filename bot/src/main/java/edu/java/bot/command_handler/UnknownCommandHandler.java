package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.UnknownCommand;

public class UnknownCommandHandler extends CommandHandler {

    @Override
    public ChatCommand handle(String text) {
        return new UnknownCommand();
    }
}
