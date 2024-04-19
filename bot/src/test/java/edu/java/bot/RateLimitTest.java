package edu.java.bot;

import edu.java.bot.services.LinkUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("rate-limit")
@EnableCaching
public class RateLimitTest extends KafkaEnvironment {

    @Autowired
    MockMvc mvc;
    @MockBean
    LinkUpdateService linkUpdateService;
    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    public void clearCache() {
        cacheManager.getCache("rate-limit-bucket").clear();
    }

    @Test
    public void testLimitExceeded() throws Exception {
        //then
        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""

                    {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
            """)).andExpect(status().isOk());
        }

        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""

                    {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
            """)).andExpect(status().is(429));
    }

    @Test
    public void testLimitRefillAfterTime() throws Exception {
        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""

                    {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
            """)).andExpect(status().isOk());
        }

        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""

                    {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
            """)).andExpect(status().is(429));

        //time passes
        clearCache();

        mvc.perform(post("/api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""

                    {
                  "id": 0,
                  "url": "https://github.com/",
                  "description": "new update",
                  "tgChatIds": [
                    1
                  ]
                }
            """)).andExpect(status().isOk());
    }
}
