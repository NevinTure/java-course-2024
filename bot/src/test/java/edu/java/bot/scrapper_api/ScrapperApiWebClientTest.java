package edu.java.bot.scrapper_api;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.clients.scrapper_api.ScrapperApiClient;
import edu.java.bot.dtos.scrapper_api.AddLinkRequest;
import edu.java.bot.dtos.scrapper_api.LinkResponse;
import edu.java.bot.dtos.scrapper_api.ListLinksResponse;
import edu.java.bot.dtos.scrapper_api.RemoveLinkRequest;
import edu.java.bot.exceptions.ApiBadRequestException;
import edu.java.bot.exceptions.ApiNotFoundException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest("app.scrapper-api-base-url=http://localhost:8080")
@WireMockTest(httpPort = 8080)
public class ScrapperApiWebClientTest {

    private final ScrapperApiClient scrapperClient;

    @Autowired
    public ScrapperApiWebClientTest(ScrapperApiClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Test
    public void testChatRegistrationWhen200Ok() {
        //given
        long id = 1L;

        //when
        stubFor(post("/api/tg-chat/" + id).willReturn(status(200)));

        //then
        assertThatNoException().isThrownBy(() -> scrapperClient.registerChat(id));
    }

    @Test
    public void testChatRegistrationWhen400BadRequest() {
        //given
        long id = 1L;

        //when
        stubFor(post("/api/tg-chat/" + id).willReturn(jsonResponse("""
            {
                "description": "Чат с id 1 уже зарегистрирован",
                "code": "400",
                "exceptionName": "ChatAlreadyRegisteredException",
                "exceptionMessage": null,
                "stackTrace": []
            }
            """, 400)));

        assertThatExceptionOfType(ApiBadRequestException.class)
            .isThrownBy(() -> scrapperClient.registerChat(id));
    }

    @Test
    public void testDeleteChatWhen200Ok() {
        //given
        long id = 1L;

        //when
        stubFor(delete("/api/tg-chat/" + id).willReturn(status(HttpStatus.OK.value())));

        //then
        assertThatNoException().isThrownBy(() -> scrapperClient.deleteChat(id));
    }

    @Test
    public void testDeleteChatWhen400BadRequest() {
        //given
        long id = -1L;

        //when
        stubFor(delete("/api/tg-chat/" + id).willReturn(jsonResponse("""
            {
                "description": "deleteChat.id: must be grater or equal to 0",
                "code": "400",
                "exceptionName": "ConstrainViolationException",
                "exceptionMessage": null,
                "stackTrace": []
            }
            """, 400)));

        assertThatExceptionOfType(ApiBadRequestException.class)
            .isThrownBy(() -> scrapperClient.deleteChat(id));
    }

    @Test
    public void testDeleteChatWhen404NotFound() {
        //given
        long id = 1L;

        //when
        stubFor(delete("/api/tg-chat/" + id).willReturn(jsonResponse("""
            {
                "description": "Чат с id 1 не найден",
                "code": "404",
                "exceptionName": "ChatNotFoundException",
                "exceptionMessage": null,
                "stackTrace": []
            }
            """, 404)));

        assertThatExceptionOfType(ApiNotFoundException.class)
            .isThrownBy(() -> scrapperClient.deleteChat(id));
    }

    @Test
    public void testGetLinkByChatIdWhen200Ok() {
        //given
        long id = 1L;
        ListLinksResponse exceptedResult = new ListLinksResponse(
            List.of(
                new LinkResponse(1, URI.create("https://editor.swagger.io/")),
                new LinkResponse(2, URI.create("https://vk.com/"))
            ));

        //when
        stubFor(get("/api/links")
            .withHeader("id", equalTo("1")).willReturn(okJson("""
                {
                    "links" : [
                    {
                        "id" : 1,
                        "url" : "https://editor.swagger.io/"
                    },
                    {
                        "id" : 2,
                        "url" : "https://vk.com/"
                    }
                    ]
                }
                """)));
        ListLinksResponse result = scrapperClient.getLinksById(id);

        //then
        assertThat(result).isEqualTo(exceptedResult);
    }

    @Test
    public void testAddLinkByChatIdWhen200Ok() {
        //given
        long id = 1L;
        AddLinkRequest request = new AddLinkRequest("https://editor.swagger.io/");
        LinkResponse expectedResult =
            new LinkResponse(1, URI.create("https://editor.swagger.io/"));

        //when
        stubFor(post("/api/links")
            .withHeader("id", equalTo("1")).willReturn(okJson("""
                {
                    "id" : 1,
                    "url" : "https://editor.swagger.io/"
                }
                """)));
        LinkResponse result = scrapperClient.addLinksByChatId(id, request);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testDeleteLinkByChatIdWhen200Ok() {
        //given
        long id = 1L;
        RemoveLinkRequest request = new RemoveLinkRequest("https://editor.swagger.io/");
        LinkResponse expectedResult =
            new LinkResponse(1, URI.create("https://editor.swagger.io/"));

        //when
        stubFor(delete("/api/links")
            .withHeader("id", equalTo("1")).willReturn(okJson("""
                {
                    "id" : 1,
                    "url" : "https://editor.swagger.io/"
                }
                """)));
        LinkResponse result = scrapperClient.deleteLinkByChatId(id, request);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }
}