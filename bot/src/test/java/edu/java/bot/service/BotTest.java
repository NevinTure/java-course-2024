package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.chat_command.ListCommand;
import edu.java.bot.command_handler.CommandHandler;
import edu.java.bot.command_handler.HelpCommandHandler;
import edu.java.bot.command_handler.ListCommandHandler;
import edu.java.bot.command_handler.StartCommandHandler;
import edu.java.bot.command_handler.TrackCommandHandler;
import edu.java.bot.command_handler.UnknownCommandHandler;
import edu.java.bot.command_handler.UntrackCommandHandler;
import edu.java.bot.model.Person;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BotTest {

    public static CommandHandler handler() {
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

//    @Test
//    public void testListMessageWhenLinkListIsEmpty() {
//        //given
//        String expectedResult = "Вы пока не отслеживаете ни одной ссылки.";
//        Person person = new Person(1L);
//
//        //when
//        SendMessage result = new ListCommand().getMessage(person, null);
//        String resultText = (String) result.getParameters().get("text");
//
//        //then
//        assertThat(resultText.trim()).isEqualTo(expectedResult);
//    }
//
//    @Test
//    public void testListMessageWhenLinkListIsNotEmpty() {
//        //given
//        String expectedResult = """
//            Отслеживаемые ссылки:
//            https://vk.com/feed
//            https://stackoverflow.com/""";
//        Person person = new Person(1L);
//        person.getLinkList().add("https://vk.com/feed");
//        person.getLinkList().add("https://stackoverflow.com/");
//
//        //when
//        SendMessage result = new ListCommand().getMessage(person, null);
//        String resultText = (String) result.getParameters().get("text");
//
//        //then
//        assertThat(resultText.trim()).isEqualTo(expectedResult);
//    }
//
//    @Test
//    public void testMessageWhenWrongInputCommand() {
//        //given
//        String wrongCommand = "/unhandled command";
//        Person person = new Person(1L);
//        String expectedResult = """
//            Неизвестная команда.
//            """;
//        CommandHandler handler = handler();
//
//        //when
//        ChatCommand command = handler.handle(wrongCommand);
//        SendMessage result = command.getMessage(person, null);
//        String resultText = (String) result.getParameters().get("text");
//
//        //then
//        assertThat(resultText).isEqualTo(expectedResult);
//    }
}
