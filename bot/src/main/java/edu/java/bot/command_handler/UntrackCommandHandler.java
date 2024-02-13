package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.UntrackCommand;

public class UntrackCommandHandler extends CommandHandler {

    @Override
    public ChatCommand handle(String text) {
        if (text.equals("/untrack")) {
            return new UntrackCommand();
        }
        return getNext().handle(text);
    }
}
