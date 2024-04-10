package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.models.exceptions.ApiBadRequestException;
import edu.java.models.exceptions.ApiInternalServerErrorException;
import edu.java.scrapper.clients.bot_api.BotApiClient;
import edu.java.scrapper.configuration.LinearRetryConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;

@WireMockTest(httpPort = 8080)
@EnableRetry
@SpringBootTest(properties = {"app.retry-policy.mode=linear",
    "app.retry-policy.codes[0]=500",
    "app.retry-policy.codes[1]=502",
    "app.retry-policy.codes[2]=504"})
public class LinearRetryTest extends IntegrationEnvironment {

    @Autowired
    private BotApiClient apiClient;
    @SpyBean(name = "botClient")
    private WebClient botClient;

    @Test
    public void testMethodWithoutRetry() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest();

        //when
        stubFor(post("/api/updates").willReturn(jsonResponse(
            """
                {}
                """,
            200
        )));
        apiClient.sendUpdate(request);

        //then
        Mockito.verify(botClient, times(1)).post();
    }

    @Test
    public void testMethodWithRetry() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest();

        //when
        stubFor(post("/api/updates").willReturn(jsonResponse(
            """
                {}
                """,
            500
        )));

        //then
        assertThatExceptionOfType(ApiInternalServerErrorException.class).isThrownBy(() -> apiClient.sendUpdate(request));
        Mockito.verify(botClient, times(LinearRetryConfig.MAX_ATTEMPTS)).post();
    }
}
