package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.GitHubResponse;
import edu.java.scrapper.utils.WebClientWrapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GitHubClientImpl implements GitHubClient {

    private final WebClient githubClient;

    public GitHubClientImpl(WebClient githubClient) {
        this.githubClient = githubClient;
    }

    @Retryable(interceptor = "interceptor")
    @Override
    public List<GitHubResponse> getUpdateInfo(String urn) {
        return WebClientWrapper.withAllExceptionsHandling(githubClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(urn)
                    .path("/events")
                    .queryParam("per_page", 1)
                    .build())
            .accept(MediaType.APPLICATION_JSON)
                .retrieve())
            .bodyToMono(new ParameterizedTypeReference<List<GitHubResponse>>(){})
            .onErrorReturn(new ArrayList<>())
            .block();
    }
}
