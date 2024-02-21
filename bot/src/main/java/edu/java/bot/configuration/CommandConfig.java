package edu.java.bot.configuration;

import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.HelpCommand;
import edu.java.bot.chat_command.ListCommand;
import edu.java.bot.chat_command.StartCommand;
import edu.java.bot.chat_command.TrackCommand;
import edu.java.bot.chat_command.UntrackCommand;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class CommandConfig {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public ChatCommand trackCommand() {
        return new TrackCommand();
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public ChatCommand untrackCommand() {
        return new UntrackCommand();
    }

    @Bean
    public ChatCommand helpCommand(List<ChatCommand> commandList) {
        return new HelpCommand(commandList);
    }

    @Bean
    public ChatCommand listCommand() {
        return new ListCommand();
    }

    @Bean
    public ChatCommand startCommand() {
        return new StartCommand();
    }
}
