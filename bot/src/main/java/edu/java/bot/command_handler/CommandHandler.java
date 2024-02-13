package edu.java.bot.command_handler;

import edu.java.bot.chat_command.ChatCommand;
import lombok.Data;

@Data
public abstract class CommandHandler {

    private CommandHandler next;

    public abstract ChatCommand handle(String text);

}
