package edu.java.bot.configuration;

import edu.java.bot.command_handler.CommandHandler;
import edu.java.bot.command_handler.HelpCommandHandler;
import edu.java.bot.command_handler.ListCommandHandler;
import edu.java.bot.command_handler.StartCommandHandler;
import edu.java.bot.command_handler.TrackCommandHandler;
import edu.java.bot.command_handler.UnknownCommandHandler;
import edu.java.bot.command_handler.UntrackCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean
    public CommandHandler commandHandler() {
        CommandHandler start = new StartCommandHandler();
        CommandHandler help = new HelpCommandHandler();
        CommandHandler list = new ListCommandHandler();
        CommandHandler track = new TrackCommandHandler();
        CommandHandler untrack = new UntrackCommandHandler();
        CommandHandler unknown = new UnknownCommandHandler();
        start.setNext(help);
        help.setNext(list);
        list.setNext(track);
        track.setNext(untrack);
        untrack.setNext(unknown);
        return start;
    }

//    @Bean
//    public InteractiveCommandHandler startHandler() {
//        InteractiveCommandHandler start = new StartInteractiveCommandHandler();
//        InteractiveCommandHandler unknown = new UnknownInteractiveCommandHandler();
//        start.setNext(unknown);
//        return start;
//    }
}
