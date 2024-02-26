package edu.java.bot.clients;

import edu.java.bot.dtos.LinkUpdateRequest;
import edu.java.bot.exceptions.ApiBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotApiClientImpl implements BotApiClient {

    private final WebClient botClient;

    public BotApiClientImpl(WebClient botClient) {
        this.botClient = botClient;
    }

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
