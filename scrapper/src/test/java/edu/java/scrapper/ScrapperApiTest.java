package edu.java.scrapper;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.scrapper.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.exceptions.ChatNotFoundException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.LinkNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.ChatService;
import java.net.URI;
import java.util.List;
import edu.java.scrapper.services.LinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ScrapperApiTest extends IntegrationEnvironment {

    @MockBean
    private ChatLinkService chatLinkService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void testChatRegistrationWithCorrectData() throws Exception {
        //given
        long id = 1L;

        //then
        mvc.perform(post("/api/tg-chat/" + id))
            .andExpect(status().isOk());
    }

    @Test
    public void testSecondChatRegistrationHandle() throws Exception {
        //given
        long id = 1L;

        //when
        Mockito.doThrow(new ChatAlreadyRegisteredException(id)).when(chatLinkService).register(id);

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
        //then
        mvc.perform(delete("/api/tg-chat/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteChatWithMissingChat() throws Exception {
        //given
        long id = 1L;

        //when
        Mockito.doThrow(new ChatNotFoundException(id)).when(chatLinkService).unregister(id);

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

        //when
        Mockito.when(chatLinkService.getLinksById(id))
            .thenReturn(new ResponseEntity<>(new ListLinksResponse(List.of(
                new LinkResponse(1, URI.create("https://stackoverflow.com/")),
                new LinkResponse(2, URI.create("https://github.com/"))
            )), HttpStatus.OK));

        //then
        mvc.perform(get("/api/links").header("id", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].url").value("https://stackoverflow.com/"))
            .andExpect(jsonPath("$.links[1].url").value("https://github.com/"));
    }

    @Test
    public void testGetLinksWithMissingChat() throws Exception {
        //given
        long id = 1L;

        //when
        Mockito.doThrow(new ChatNotFoundException(id)).when(chatLinkService).getLinksById(id);

        //then
        mvc.perform(get("/api/links").header("id", id))
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
        String urlStr = "https://stackoverflow.com/";

        //when
        Mockito.when(chatLinkService
            .addLink(id, new AddLinkRequest("https://stackoverflow.com/")))
            .thenReturn(new ResponseEntity<>(new LinkResponse(id, URI.create(urlStr)), HttpStatus.OK));

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
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.url").value("https://stackoverflow.com/"));
    }

    @Test
    public void testAddLinkIfLinkAlreadyExists() throws Exception {
        //given
        long id = 1L;
        String urlStr = "https://stackoverflow.com/";

        //when
        Mockito.doThrow(new LinkAlreadyTrackedException(id, URI.create(urlStr)))
            .when(chatLinkService).addLink(id, new AddLinkRequest(urlStr));

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
        String urlStr = "https://stackoverflow.com/";

        //when
        Mockito.when(chatLinkService.removeLink(id, new RemoveLinkRequest(urlStr)))
            .thenReturn(new ResponseEntity<>(new LinkResponse(id, URI.create(urlStr)), HttpStatus.OK));

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
    }

    @Test
    public void testDeleteLinkIfLinkNotTrackingByChat() throws Exception {
        //given
        long id = 1L;
        String urlStr = "https://stackoverflow.com/";

        //when
        Mockito.doThrow(new LinkNotFoundException(id, URI.create(urlStr))).when(chatLinkService)
            .removeLink(id, new RemoveLinkRequest(urlStr));

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
