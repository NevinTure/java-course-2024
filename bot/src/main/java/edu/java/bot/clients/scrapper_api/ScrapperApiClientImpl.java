package edu.java.bot.clients.scrapper_api;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.models.dtos.TgChatDto;
import edu.java.models.exceptions.ApiBadRequestException;
import edu.java.models.exceptions.ApiNotFoundException;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
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

    @Retryable(interceptor = "interceptor")
    @Override
    public void registerChat(long id) {
        System.out.println(System.currentTimeMillis());
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

    @Retryable(interceptor = "interceptor")
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

    @Retryable(interceptor = "interceptor")
    @Override
    public Optional<TgChatDto> getChatById(long id) {
        return scrapperClient
            .get()
            .uri(uriBuilder -> uriBuilder.path("/api/tg-chat/{id}").build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                v -> v.equals(HttpStatus.NOT_FOUND),
                response -> Mono.error(new ApiNotFoundException("", "ChatNotFoundException"))
            )
            .bodyToMono(TgChatDto.class)
            .onErrorResume(ApiNotFoundException.class, notFound -> Mono.empty())
            .blockOptional();
    }

    @Retryable(interceptor = "interceptor")
    @Override
    public void updateChatById(long id, TgChatDto dto) {
        scrapperClient
            .put()
            .uri(uriBuilder -> uriBuilder.path("/api/tg-chat/{id}").build(id))
            .bodyValue(dto)
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

    @Retryable(interceptor = "interceptor")
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

    @Retryable(interceptor = "interceptor")
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

    @Retryable(interceptor = "interceptor")
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
