package edu.java.scrapper;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.services.ChatService;
import java.net.URI;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ScrapperApiTest extends IntegrationEnvironment {

    private final ChatService chatService;
    private final MockMvc mvc;

    @Autowired
    public ScrapperApiTest(ChatService chatService, MockMvc mvc) {
        this.chatService = chatService;
        this.mvc = mvc;
    }

    @Test
    public void testChatRegistrationWithCorrectData() throws Exception {
        //given
        long id = 1L;

        //then
        mvc.perform(post("/api/tg-chat/" + id))
            .andExpect(status().isOk());
        assertThat(chatService.getById(id)).isPresent();
    }

    @Test
    public void testSecondChatRegistrationHandle() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);

        //when
        chatService.save(chat);

        //then
        mvc.perform(post("/api/tg-chat/" + id))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Чат с id 1 уже зарегистрирован"));
    }

    @Test
    public void testChatRegistrationWithInvalidData() throws Exception {
        //then
        mvc.perform(post("/api/tg-chat/-1"))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("registerChat.id: must be greater than or equal to 0"));
    }

    @Test
    public void testDeleteChatWithCorrectData() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);

        //when
        chatService.save(chat);

        //then
        mvc.perform(delete("/api/tg-chat/1"))
            .andExpect(status().isOk());
        assertThat(chatService.getById(id)).isNotPresent();
    }

    @Test
    public void testDeleteChatWithMissingChat() throws Exception {
        //then
        mvc.perform(delete("/api/tg-chat/1"))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Чат с id 1 не найден"));
    }

    @Test
    public void testDeleteChatWithInvalidData() throws Exception {
        //then
        mvc.perform(delete("/api/tg-chat/-1"))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("deleteChat.id: must be greater than or equal to 0"));
    }

    @Test
    public void testGetLinksWithCorrectData() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);
        chat.getLinkList().add(new Link(0, URI.create("https://stackoverflow.com/")));
        chat.getLinkList().add(new Link(1, URI.create("https://github.com/")));

        //when
        chatService.save(chat);

        //then
        mvc.perform(get("/api/links").header("id", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].url").value("https://stackoverflow.com/"))
            .andExpect(jsonPath("$.links[1].url").value("https://github.com/"));
    }

    @Test
    public void testGetLinksWithMissingChat() throws Exception {
        //then
        mvc.perform(get("/api/links").header("id", 1))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Чат с id 1 не найден"));
    }

    @Test
    public void testGetLinksWithInvalidData() throws Exception {
        //then
        mvc.perform(get("/api/links").header("id", -1))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("getLinksByChatId.id: must be greater than or equal to 0"));
    }

    @Test
    public void testAddLinksWithCorrectData() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);
        Link link = new Link(0, URI.create("https://stackoverflow.com/"));

        //when
        chatService.save(chat);

        //then
        mvc.perform(post("/api/links")
                .header("id", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "url" : "https://stackoverflow.com/"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(0))
            .andExpect(jsonPath("$.url").value("https://stackoverflow.com/"));
        assertThat(chatService.getById(id).get().getLinkList()).containsExactly(link);
    }

    @Test
    public void testAddLinkIfLinkAlreadyExists() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);
        Link link = new Link(0, URI.create("https://stackoverflow.com/"));
        chat.getLinkList().add(link);

        //when
        chatService.save(chat);

        //then
        mvc.perform(post("/api/links")
                .header("id", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "url" : "https://stackoverflow.com/"
                    }
                    """))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Чат с id 1 уже отслеживает ссылку https://stackoverflow.com/"));
    }

    @Test
    public void testDeleteLinkWithCorrectData() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);
        Link link = new Link(1, URI.create("https://stackoverflow.com/"));
        chat.getLinkList().add(link);

        //when
        chatService.save(chat);

        //then
        mvc.perform(delete("/api/links")
                .header("id", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "url" : "https://stackoverflow.com/"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.url").value("https://stackoverflow.com/"));
        assertThat(chatService.getById(id).get().getLinkList()).isEmpty();
    }

    @Test
    public void testDeleteLinkIfLinkNotTrackingByChat() throws Exception {
        //given
        long id = 1L;
        TgChat chat = new TgChat(id);

        //when
        chatService.save(chat);

        //then
        mvc.perform(delete("/api/links")
                .header("id", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url" : "https://stackoverflow.com/"
                        }
                    """))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.description")
                .value("Чат с id 1 не отслеживает ссылку https://stackoverflow.com/"));
    }
}
