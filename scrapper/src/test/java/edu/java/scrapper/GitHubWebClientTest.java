package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.GitHubClient;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("app.git-base-url=http://localhost:8080")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@WireMockTest(httpPort = 8080)
public class GitHubWebClientTest {

    private final GitHubClient gitHubClient;

    @Autowired
    public GitHubWebClientTest(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Test
    public void testGitClientWhenValidURI() {
        //given
        String uri = "/test_user/test_repo";
        OffsetDateTime expectedResult = OffsetDateTime.parse("2011-01-26T19:14:43Z");

        //when
        stubFor(get(uri)
            .willReturn(okJson("{ \"updated_at\": \"2011-01-26T19:14:43Z\" }")));

        //then
        assertThat(gitHubClient.getUpdateInfo(uri).getDateTime()).isEqualTo(expectedResult);
    }

    @Test
    public void testGitClientWhenInvalidURI() {
        //given
        String uri = "invalid";

        //then
        assertThat(gitHubClient.getUpdateInfo(uri).getDateTime()).isNull();
    }
}
