package edu.java.bot.clients;

import edu.java.bot.dtos.ApiErrorResponse;
import edu.java.bot.dtos.LinkUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotApiClientImpl implements BotApiClient {

    private final WebClient botClient;

    public BotApiClientImpl(WebClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public ApiErrorResponse sendUpdate(LinkUpdateRequest request) {
        return botClient
            .post()
            .uri("/api/updates")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return Mono.empty();
                }
                return response.body(BodyExtractors.toMono(ApiErrorResponse.class));
            }).block();
    }
}
