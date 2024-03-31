package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.StackOverflowResponse;
import java.time.Duration;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
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

    @Retryable(interceptor = "interceptor")
    @Override
    public StackOverflowResponse getUpdateInfo(List<String> urns) {
        return stackOverflowClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(String.join(";", urns))
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
