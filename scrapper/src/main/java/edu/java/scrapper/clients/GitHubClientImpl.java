package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.GitHubResponse;
import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Component
public class GitHubClientImpl implements GitHubClient {

    private final WebClient githubClient;
    private static final int MAX_ATTEMPTS = 3;

    public GitHubClientImpl(WebClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
    public GitHubResponse getUpdateInfo(String uri) {
        return githubClient
            .get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .onErrorReturn(new GitHubResponse())
            .retryWhen(Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofSeconds(1)))
            .block();
    }
}
