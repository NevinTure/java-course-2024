package edu.java.bot.utils;

import edu.java.models.exceptions.ApiBadRequestException;
import edu.java.models.exceptions.ApiInternalServerErrorException;
import edu.java.models.exceptions.ApiNotFoundException;
import edu.java.models.exceptions.ApiServerBadGatewayException;
import edu.java.models.exceptions.ApiServerGatewayTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientWrapper {

    private WebClientWrapper() {
    }

    public static WebClient.ResponseSpec withAllExceptionsHandling(WebClient.ResponseSpec spec) {
        return spec
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
            .onStatus(
                v -> v.equals(HttpStatus.INTERNAL_SERVER_ERROR),
                response -> response.bodyToMono(ApiInternalServerErrorException.class)
                    .flatMap(Mono::error)
            )
            .onStatus(
                v -> v.equals(HttpStatus.BAD_GATEWAY),
                response -> response.bodyToMono(ApiServerBadGatewayException.class)
                    .flatMap(Mono::error)
            )
            .onStatus(
                v -> v.equals(HttpStatus.GATEWAY_TIMEOUT),
                response -> response.bodyToMono(ApiServerGatewayTimeoutException.class)
                    .flatMap(Mono::error)
            );
    }
}
