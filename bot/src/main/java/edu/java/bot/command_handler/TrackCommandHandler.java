package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.TrackCommand;

public class TrackCommandHandler extends CommandHandler {

    @Override
    public ChatCommand handle(String text) {
        if (text.equals("/track")) {
            return new TrackCommand();
        }
        return getNext().handle(text);
    }
}
