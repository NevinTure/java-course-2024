package edu.java.clients;

import edu.java.dtos.StackOverflowResponse;
import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {

    private final WebClient stackOverflowClient;
    private static final int MAX_ATTEMPTS = 3;

    public StackOverflowClientImpl(WebClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public StackOverflowResponse getUpdateInfo(String uri) {
        return stackOverflowClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(uri)
                    .queryParam("site", "stackoverflow")
                    .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .onErrorReturn(new StackOverflowResponse())
            .retryWhen(Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofSeconds(1)))
            .block();
    }
}
