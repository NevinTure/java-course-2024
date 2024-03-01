package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.StackOverflowClient;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("app.sof-base-url=http://localhost:8080")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@WireMockTest(httpPort = 8080)
public class StackOverflowWebClientTest {

    private final StackOverflowClient stackOverflowClient;

    @Autowired
    public StackOverflowWebClientTest(StackOverflowClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Test
    public void testSofClientWhenValidUri() {
        //given
        String uri = "/1111111";
        OffsetDateTime expectedResult = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1645471009), ZoneOffset.UTC);
        //when
        stubFor(get(uri + "?site=stackoverflow")
            .willReturn(okJson("{ \"items\" : [ {\"last_activity_date\" : 1645471009 } ] }")));

        //then
        assertThat(stackOverflowClient.getUpdateInfo(uri).getItems().get(0).getDateTime()).isEqualTo(expectedResult);
    }

    @Test
    public void testSofClientWhenInvalidUri() {
        //given
        String uri = "invalid";

        //then
        assertThat(stackOverflowClient.getUpdateInfo(uri).getItems()).isNull();
    }
}
