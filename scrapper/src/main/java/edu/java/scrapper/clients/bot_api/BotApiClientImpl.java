package edu.java.scrapper.clients.bot_api;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.models.exceptions.ApiBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotApiClientImpl implements BotApiClient {

    private final WebClient botClient;

    public BotApiClientImpl(WebClient botClient) {
        this.botClient = botClient;
    }

    @Retryable(interceptor = "interceptor")
    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        botClient
            .post()
            .uri("/api/updates")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiBadRequestException.class)
                    .flatMap(Mono::error)
            )
            .bodyToMono(Void.class)
            .block();
    }
}
