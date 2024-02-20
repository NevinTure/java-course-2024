package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.command_handler.CommandHandler;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommandTest {

    private final CommandHandler commandHandler;
    private final ChatService chatService;

    @Autowired
    public CommandTest(CommandHandler commandHandler, ChatService chatService) {
        this.commandHandler = commandHandler;
        this.chatService = chatService;
    }

    @Test
    public void testListMessageWhenLinkListIsEmpty() {
        //given
        long id = 1L;
        String expectedResult = "Вы пока не отслеживаете ни одной ссылки.";

        //when
        chatService.save(new Person(1L));
        SendMessage result = commandHandler.handle(id, "/list").getMessage(id);
        String resultText = (String) result.getParameters().get("text");

        //then
        assertThat(resultText.trim()).isEqualTo(expectedResult);
    }

    @Test
    public void testListMessageWhenLinkListIsNotEmpty() {
        //given
        long id = 1L;
        String expectedResult = """
            Отслеживаемые ссылки:

            1. https://vk.com/feed

            2. https://stackoverflow.com/""";
        Person person = new Person(id);
        person.getLinkList().add("https://vk.com/feed");
        person.getLinkList().add("https://stackoverflow.com/");

        //when
        chatService.save(person);
        SendMessage result = commandHandler.handle(id, "/list").getMessage(id);
        String resultText = (String) result.getParameters().get("text");

        //then
        assertThat(resultText.trim()).isEqualTo(expectedResult);
    }

    @Test
    public void testMessageWhenWrongInputCommand() {
        //given
        long id = 1L;
        String wrongCommand = "/unhandled command";
        Person person = new Person(id);
        String expectedResult = """
            Неизвестная команда.""";

        //when
        chatService.save(person);
        ChatCommand command = commandHandler.handle(id, wrongCommand);
        SendMessage result = command.getMessage(id);
        String resultText = (String) result.getParameters().get("text");

        //then
        assertThat(resultText).isEqualTo(expectedResult);
    }

    @Test
    public void testMessageWhenUnregisteredPerson() {
        //given
        String listCommand = "/list";
        long unregisteredId = 1L;
        String expectedResult = """
            Неизвестная команда.""";

        //when
        SendMessage result = commandHandler.handle(unregisteredId, listCommand).getMessage(unregisteredId);
        String resultText = (String) result.getParameters().get("text");

        //then
        assertThat(resultText).isEqualTo(expectedResult);
    }

    @Test
    public void testTrackUrl() {
        //given
        long id = 1L;
        String url = "https://vk.com/feed";
        Person person = new Person(id);
        String expectedResult1 = """
            Введите ссылку, которую хотите начать отслеживать.
            Введите /cancel чтобы отменить действие.
            """;
        String expectedResult2 = "Ссылка добавлена для отслеживания.";

        //when
        chatService.save(person);
        SendMessage result = commandHandler.handle(id, "/track").getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, url).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
        assertThat(person.getLinkList()).containsExactly(url);
    }

    @Test
    public void testTrackUrlDialog() {
        //given
        long id = 1L;
        String url = "incorrect url";
        String cancelOp = "/cancel";
        Person person = new Person(id);
        String expectedResult1 = "Некорректная ссылка.";
        String expectedResult2 = "Отмена.";

        //when
        chatService.save(person);
        commandHandler.handle(id, "/track");
        SendMessage result = commandHandler.handle(id, url).getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, cancelOp).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
        assertThat(person.getLinkList()).isEmpty();
    }

    @Test
    public void testUntrackUrl() {
        //given
        long id = 1L;
        String url = "https://vk.com/feed";
        Person person = new Person(id);
        person.getLinkList().add(url);
        String expectedResult1 = """
            Введите ссылку, которую хотите прекратить отслеживать.
            Введите /cancel чтобы отменить действие.
            """;
        String expectedResult2 = "Отслеживание ссылки прекращено.";

        //when
        chatService.save(person);
        SendMessage result = commandHandler.handle(id, "/untrack").getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, url).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
        assertThat(person.getLinkList()).isEmpty();
    }

    @Test
    public void testUntrackUrlDialog() {
        //given
        long id = 1L;
        String url = "incorrect url";
        String cancelOp = "/cancel";
        Person person = new Person(id);
        String expectedResult1 = "Некорректная ссылка.";
        String expectedResult2 = "Отмена.";

        //when
        chatService.save(person);
        commandHandler.handle(id, "/untrack");
        SendMessage result = commandHandler.handle(id, url).getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, cancelOp).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
        assertThat(person.getLinkList()).isEmpty();
    }

    @Test
    public void testStartCommand() {
        //given
        long id = 2L;
        String expectedResult = "Регистрация прошла успешно.";

        //when
        SendMessage result = commandHandler.handle(id, "/start").getMessage(id);
        String resultText = (String) result.getParameters().get("text");

        //then
        assertThat(resultText).isEqualTo(expectedResult);
        assertThat(chatService.getById(id)).isNotNull();
    }

    @Test
    public void testHelpCommand() {
        //given
        long id = 1L;
        Person person = new Person(id);
        String expectedResult = """
            /track -- начать отслеживание ссылки
            /untrack -- прекратить отслеживание ссылки
            /list -- показать список отслеживаемых ссылок
            /start -- зарегистрировать пользователя
            /help -- вывести окно с командами""";

        //when
        chatService.save(person);
        SendMessage result = commandHandler.handle(id, "/help").getMessage(id);
        String resultText = (String) result.getParameters().get("text");

        //then
        assertThat(resultText).isEqualTo(expectedResult);
    }
}
