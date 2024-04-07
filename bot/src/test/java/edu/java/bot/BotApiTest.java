package edu.java.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BotApiTest extends KafkaEnvironment{

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
