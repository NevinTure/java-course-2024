package edu.java.scrapper;

import edu.java.scrapper.services.ChatLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("rate-limit")
@AutoConfigureMockMvc
@EnableCaching
public class RateLimitTest extends IntegrationEnvironment {

    @Autowired
    MockMvc mvc;
    @MockBean
    private ChatLinkService chatLinkService;
    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    public void clearCache() {
        cacheManager.getCache("rate-limit-bucket").clear();
    }

    @Test
    public void testLimitExceeded() throws Exception {
        //given
        long id = 1L;

        //then
        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/tg-chat/" + id))
                .andExpect(status().isOk());
        }

        mvc.perform(post("/api/tg-chat/" + id))
            .andExpect(status().is(429));
    }

    @Test
    public void testLimitRefillAfterTime() throws Exception {
        //given
        long id = 1L;

        //then
        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/tg-chat/" + id))
                .andExpect(status().isOk());
        }

        mvc.perform(post("/api/tg-chat/" + id))
            .andExpect(status().is(429));

        //time passes
        clearCache();

        mvc.perform(post("/api/tg-chat/" + id))
            .andExpect(status().isOk());
    }
}
