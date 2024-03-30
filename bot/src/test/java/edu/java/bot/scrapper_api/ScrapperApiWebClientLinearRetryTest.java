package edu.java.bot.scrapper_api;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.clients.scrapper_api.ScrapperApiClient;
import edu.java.bot.configuration.LinearRetryConfig;
import edu.java.models.exceptions.ApiBadRequestException;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.verify;

@EnableRetry
@SpringBootTest(properties = {"app.retry-policy.mode=linear",
    "app.retry-policy.codes[0]=not_found",
    "app.retry-policy.codes[1]=bad_request"})
@WireMockTest(httpPort = 8080)
public class ScrapperApiWebClientLinearRetryTest {

    @Autowired
    private ScrapperApiClient scrapperApi;
    @SpyBean
    private WebClient scrapperClient;

    @Test
    public void testMethodWithoutRetries() {
        //given
        long id = 1L;

        //when
        stubFor(post("/api/tg-chat/" + id).willReturn(jsonResponse(
            """
                {}
                """,
            200
        )));
        scrapperApi.registerChat(id);

        //then
        verify(scrapperClient, times(1)).post();
    }

    @Test
    public void testMethodWithRetries() {
        //given
        long id = 1L;

        //when
        stubFor(post("/api/tg-chat/" + id).willReturn(jsonResponse(
            """
                {}
                """,
            400
        )));

        //then
        assertThatExceptionOfType(ApiBadRequestException.class).isThrownBy(() -> scrapperApi.registerChat(id));
        verify(scrapperClient, times(LinearRetryConfig.MAX_ATTEMPTS)).post();
    }
}
