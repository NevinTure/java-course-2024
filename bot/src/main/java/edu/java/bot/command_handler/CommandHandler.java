package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;

public interface CommandHandler {

    ChatCommand handle(long senderId, String text);
}
