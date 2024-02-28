package edu.java.bot.clients.scrapper_api;

import edu.java.bot.dtos.scrapper_api.AddLinkRequest;
import edu.java.bot.dtos.scrapper_api.LinkResponse;
import edu.java.bot.dtos.scrapper_api.ListLinksResponse;
import edu.java.bot.dtos.scrapper_api.RemoveLinkRequest;
import edu.java.bot.exceptions.ApiBadRequestException;
import edu.java.bot.exceptions.ApiNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")
@Component
public class ScrapperApiClientImpl implements ScrapperApiClient {

    private final WebClient scrapperClient;

    public ScrapperApiClientImpl(WebClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public void registerChat(long id) {
        scrapperClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("/api/tg-chat/{id}").build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiBadRequestException.class)
                    .flatMap(Mono::error)
            )
            .bodyToMono(Void.class)
            .block();
    }

    @Override
    public void deleteChat(long id) {
        scrapperClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path("/api/tg-chat/{id}").build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiBadRequestException.class)
                    .flatMap(Mono::error)
            )
            .onStatus(
                v -> v.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiNotFoundException.class)
                    .flatMap(Mono::error)
            )
            .bodyToMono(Void.class)
            .block();
    }

    @Override
    public ListLinksResponse getLinksById(long id) {
        return scrapperClient
            .get()
            .uri("/api/links")
            .header("id", String.valueOf(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiBadRequestException.class)
                    .flatMap(Mono::error)
            )
            .onStatus(
                v -> v.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiNotFoundException.class)
                    .flatMap(Mono::error)
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    @Override
    public LinkResponse addLinksByChatId(long id, AddLinkRequest request) {
        return scrapperClient
            .post()
            .uri("/api/links")
            .bodyValue(request)
            .header("id", String.valueOf(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiBadRequestException.class)
                    .flatMap(Mono::error)
            )
            .onStatus(
                v -> v.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiNotFoundException.class)
                    .flatMap(Mono::error)
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Override
    public LinkResponse deleteLinkByChatId(long id, RemoveLinkRequest request) {
        return scrapperClient
            .method(HttpMethod.DELETE)
            .uri("/api/links")
            .bodyValue(request)
            .header("id", String.valueOf(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiBadRequestException.class)
                    .flatMap(Mono::error)
            )
            .onStatus(
                v -> v.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiNotFoundException.class)
                    .flatMap(Mono::error)
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
