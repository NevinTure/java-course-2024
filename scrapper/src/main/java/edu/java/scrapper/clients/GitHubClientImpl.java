package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.GitHubResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
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
    public List<GitHubResponse> getUpdateInfo(String urn) {
        return githubClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(urn)
                    .path("/events")
                    .queryParam("per_page", 1)
                    .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<GitHubResponse>>(){})
            .onErrorReturn(new ArrayList<>())
            .retryWhen(Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofSeconds(1)))
            .block();
    }
}
