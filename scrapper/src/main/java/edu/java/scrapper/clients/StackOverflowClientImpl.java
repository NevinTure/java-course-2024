package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.StackOverflowResponse;
import edu.java.scrapper.utils.WebClientWrapper;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {

    private final WebClient stackOverflowClient;

    public StackOverflowClientImpl(WebClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Retryable(interceptor = "interceptor")
    @Override
    public StackOverflowResponse getUpdateInfo(List<String> urns) {
        return WebClientWrapper.withAllExceptionsHandling(stackOverflowClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(String.join(";", urns))
                    .queryParam("site", "stackoverflow")
                    .build()
            )
            .accept(MediaType.APPLICATION_JSON)
                .retrieve())
            .bodyToMono(StackOverflowResponse.class)
            .onErrorReturn(new StackOverflowResponse())
            .block();
    }
}
