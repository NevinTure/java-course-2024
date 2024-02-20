package edu.java.bot.configuration;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.HelpCommand;
import edu.java.bot.chat_command.ListCommand;
import edu.java.bot.chat_command.StartCommand;
import edu.java.bot.chat_command.TrackCommand;
import edu.java.bot.chat_command.UnknownCommand;
import edu.java.bot.chat_command.UntrackCommand;
import edu.java.bot.command_handler.CommandHandler;
import edu.java.bot.command_handler.HelpCommandHandler;
import edu.java.bot.command_handler.ListCommandHandler;
import edu.java.bot.command_handler.StartCommandHandler;
import edu.java.bot.command_handler.TrackCommandHandler;
import edu.java.bot.command_handler.UnknownCommandHandler;
import edu.java.bot.command_handler.UntrackCommandHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class CommandConfig {

    @Order(1)
    @Bean
    public ChatCommand trackCommand() {
        return new TrackCommand();
    }

    @Order(2)
    @Bean
    public ChatCommand untrackCommand() {
        return new UntrackCommand();
    }

    @Order(3)
    @Bean
    public ChatCommand helpCommand(List<ChatCommand> commandList) {
        return new HelpCommand(commandList);
    }

    @Order(4)
    @Bean
    public ChatCommand listCommand() {
        return new ListCommand();
    }

    @Order(5)
    @Bean
    public ChatCommand startCommand() {
        return new StartCommand();
    }

    @Order(6)
    @Bean
    public ChatCommand unknownCommand() {
        return new UnknownCommand();
    }
}
