package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.chat_command.ChatCommand;
import edu.java.bot.command_handler.CommandHandler;
import edu.java.bot.model.Link;
import edu.java.bot.model.TgChat;
import edu.java.bot.services.ChatService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CommandTest extends KafkaEnvironment {

    @Autowired
    private CommandHandler commandHandler;
    @MockBean
    private ChatService chatService;

    @Test
    public void testListMessageWhenLinkListIsEmpty() {
        //given
        long id = 1L;
        String expectedResult = "Вы пока не отслеживаете ни одной ссылки.";
        Mockito.when(chatService.getLinksById(id)).thenReturn(List.of());
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));

        //when
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
        Mockito.when(chatService.getLinksById(id))
            .thenReturn(List.of(
                new Link(0, URI.create("https://vk.com/feed")),
                new Link(0, URI.create("https://stackoverflow.com/"))
            ));
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));

        //when
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
        String expectedResult = """
            Неизвестная команда.""";
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));

        //when
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
        String url = "https://github.com/new/repo";
        String expectedResult1 = """
            Введите ссылку, которую хотите начать отслеживать.
            Введите /cancel чтобы отменить действие.
            """;
        String expectedResult2 = "Ссылка добавлена для отслеживания.";
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));
        Mockito.when(chatService.addLinksByChatId(id, new Link(URI.create(url)))).thenReturn(null);

        //when
        SendMessage result = commandHandler.handle(id, "/track").getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, url).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
    }

    @Test
    public void testTrackUrlDialog() {
        //given
        long id = 1L;
        String url = "incorrect url";
        String cancelOp = "/cancel";
        String expectedResult1 = "Некорректная ссылка.";
        String expectedResult2 = "Отмена.";
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));

        //when
        commandHandler.handle(id, "/track");
        SendMessage result = commandHandler.handle(id, url).getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, cancelOp).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
    }

    @Test
    public void testUntrackUrl() {
        //given
        long id = 1L;
        String url = "https://github.com/new/repo";
        String expectedResult1 = """
            Введите ссылку, которую хотите прекратить отслеживать.
            Введите /cancel чтобы отменить действие.
            """;
        String expectedResult2 = "Отслеживание ссылки прекращено.";
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));
        Mockito.when(chatService.deleteLinkByChatId(id, new Link(URI.create(url)))).thenReturn(null);

        //when
        SendMessage result = commandHandler.handle(id, "/untrack").getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, url).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
    }

    @Test
    public void testUntrackUrlDialog() {
        //given
        long id = 1L;
        String url = "incorrect url";
        String cancelOp = "/cancel";
        String expectedResult1 = "Некорректная ссылка.";
        String expectedResult2 = "Отмена.";
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));

        //when
        commandHandler.handle(id, "/untrack");
        SendMessage result = commandHandler.handle(id, url).getMessage(id);
        String resultText1 = (String) result.getParameters().get("text");
        result = commandHandler.handle(id, cancelOp).getMessage(id);
        String resultText2 = (String) result.getParameters().get("text");

        //then
        assertThat(resultText1).isEqualTo(expectedResult1);
        assertThat(resultText2).isEqualTo(expectedResult2);
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
    }

    @Test
    public void testHelpCommand() {
        //given
        long id = 1L;
        String[] expectedResult = {
            "/track -- начать отслеживание ссылки",
            "/untrack -- прекратить отслеживание ссылки",
            "/list -- показать список отслеживаемых ссылок",
            "/start -- зарегистрировать пользователя",
            "/help -- вывести окно с командами"
        };
        Mockito.when(chatService.getChatById(id)).thenReturn(Optional.of(new TgChat(id)));

        //when
        SendMessage result = commandHandler.handle(id, "/help").getMessage(id);
        String resultText = (String) result.getParameters().get("text");
        String[] resultCommandList = resultText.split("\n");

        //then
        assertThat(resultCommandList).containsExactlyInAnyOrder(expectedResult);
    }
}
