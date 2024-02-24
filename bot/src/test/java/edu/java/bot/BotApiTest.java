package edu.java.bot;

import edu.java.bot.model.LinkUpdate;
import edu.java.bot.model.Person;
import edu.java.bot.services.ChatService;
import edu.java.bot.services.LinkUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        Person person = new Person(id);
        person.getLinkList().add("https://github.com/");

        //when
        chatService.save(person);

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
        assertThat(linkUpdateService.getById(0)).isNotNull();
        assertThat(linkUpdateService.getById(0)).isEqualTo(new LinkUpdate(
            0,
            "https://github.com/",
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
}
