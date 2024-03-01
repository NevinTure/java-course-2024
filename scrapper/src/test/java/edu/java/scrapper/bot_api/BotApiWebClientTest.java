package edu.java.scrapper.bot_api;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.clients.bot_api.BotApiClient;
import edu.java.models.exceptions.ApiBadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

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

        //then
        assertThatExceptionOfType(ApiBadRequestException.class)
            .isThrownBy(() -> botClient.sendUpdate(request));
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

        //then
        assertThatNoException().isThrownBy(() -> botClient.sendUpdate(request));
    }
}
