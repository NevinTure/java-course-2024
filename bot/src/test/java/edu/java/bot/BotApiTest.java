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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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
public class BotApiTest {

    private final MockMvc mvc;

    @Autowired
    public BotApiTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void testUpdateWhenValidData() throws Exception {
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
    }

    @Test
    public void testUpdateWhenInvalidData() throws Exception {
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
                .value("Некорректные значения параметров запроса: url"));
    }
}
