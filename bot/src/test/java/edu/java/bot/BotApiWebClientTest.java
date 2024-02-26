package edu.java.bot;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.clients.BotApiClient;
import edu.java.bot.dtos.ApiErrorResponse;
import edu.java.bot.dtos.LinkUpdateRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("app.bot-api-base-url=http://localhost:8080")
@WireMockTest(httpPort = 8080)
public class BotApiWebClientTest {

    private final BotApiClient botClient;

    @Autowired
    public BotApiWebClientTest(BotApiClient botClient) {
        this.botClient = botClient;
    }

    @Test
    public void testBotClientUpdatesWhen400BadRequest() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            "new update",
            "https://docs.spring.io/",
            List.of(1L)
        );

        //when
        stubFor(post("/api/updates").willReturn(jsonResponse("""
            {
                "description": "Следующие Id чатов не были найдены: 1",
                "code": "400",
                "exceptionName": "ChatNotFoundException",
                "exceptionMessage": null,
                "stackTrace": []
            }
            """, 400)));
        ApiErrorResponse error = botClient.sendUpdate(request);

        //then
        assertThat(error.getDescription())
            .isEqualTo("Следующие Id чатов не были найдены: 1");
    }

    @Test
    public void testBotClientUpdatesWhen200Ok() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            "new update",
            "https://docs.spring.io/",
            List.of(1L)
        );

        //when
        stubFor(post("/api/updates").willReturn(status(HttpStatus.OK.value())));
        ApiErrorResponse error = botClient.sendUpdate(request);

        //then
        assertThat(error).isNull();
    }
}
