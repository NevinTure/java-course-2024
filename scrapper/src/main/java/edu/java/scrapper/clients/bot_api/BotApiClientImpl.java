package edu.java.scrapper.clients.bot_api;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.utils.WebClientWrapper;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BotApiClientImpl implements BotApiClient {

    private final WebClient botClient;

    public BotApiClientImpl(WebClient botClient) {
        this.botClient = botClient;
    }

    @Retryable(interceptor = "interceptor")
    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        WebClientWrapper.withAllExceptionsHandling(botClient
            .post()
            .uri("/api/updates")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve())
            .bodyToMono(Void.class)
            .block();
    }
}
