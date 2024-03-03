package edu.java.bot;

import edu.java.bot.model.Link;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.model.TgChat;
import edu.java.bot.services.ChatService;
import edu.java.bot.services.LinkUpdateService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BotApiTest {

    private final ChatService chatService;
    private final LinkUpdateService linkUpdateService;
    private final MockMvc mvc;

    @Autowired
    public BotApiTest(ChatService chatService, LinkUpdateService linkUpdateService, MockMvc mvc) {
        this.chatService = chatService;
        this.linkUpdateService = linkUpdateService;
        this.mvc = mvc;
    }

    @Test
    public void testUpdateWhenValidData() throws Exception {
        //given
        long id = 1L;
        TgChat tgChat = new TgChat(id);
        tgChat.getLinkList().add(new Link(0, URI.create("https://github.com/")));

        //when
        chatService.save(tgChat);

        //then
        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
                """
        )).andExpect(status().isOk());
        assertThat(linkUpdateService.getById(0).isPresent()).isTrue();
        assertThat(linkUpdateService.getById(0).get()).isEqualTo(new LinkUpdate(
            0,
            URI.create("https://github.com/"),
            "new update",
            List.of(id)
        ));
    }

    @Test
    public void testUpdateWhenChatNotExist() throws Exception {
        //then
        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
                """
            )).andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Следующие Id чатов не были найдены: 1"))
            .andExpect(jsonPath("$.exceptionName").value("ChatNotFoundException"));
    }

    @Test
    public void testUpdateWhenChatDontTrackProvidedUrl() throws Exception {
        //given
        long id = 2L;
        TgChat tgChat = new TgChat(id);
        tgChat.getLinkList().add(new Link(0, URI.create("https://github.com/")));

        //when
        chatService.save(tgChat);

        //then
        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "id": 0,
                  "url": "https://stackoverflow.com/",
                  "description": "new update",
                  "tgChatIds": [
                    2
                  ]
                }
                """
            )).andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Чат с Id 2 не отслеживает ссылки: https://stackoverflow.com/"));
    }

    @Test
    public void testUpdateWhenInvalidUrl() throws Exception {
        //then
        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "id": 0,
                  "url": "invalid url",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
                """
            )).andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Некорректные значения параметров запроса: url"))
            .andExpect(jsonPath("$.exceptionName")
                .value("MethodArgumentNotValidException"));
    }
}
