package edu.java.scrapper.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class StackOverflowWebClientConfig {

    private static final String BASE_URL = "https://api.stackexchange.com/2.3/questions/";
    private static final int TIMEOUT = 1000;

    @Bean
    public WebClient stackOverflowClient(ApplicationConfig applicationConfig) {
        String baseUrl = applicationConfig.sofBaseUrl() == null ? BASE_URL : applicationConfig.sofBaseUrl();
        HttpClient httpClient = HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
            .compress(true)
            .doOnConnected(conn -> {
                conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                conn.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
            });

        return WebClient
            .builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}
