package edu.java.bot.configuration.scrapper_api;

import edu.java.bot.configuration.ApplicationConfig;
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
public class ScrapperWebClientConfig {

    private static final String BASE_URL = "http://localhost:8080";
    private static final int TIMEOUT = 5000;

    @Bean
    public WebClient scrapperClient(ApplicationConfig config) {
        String baseUrl = config.scrapperApiBaseUrl() == null ? BASE_URL : config.scrapperApiBaseUrl();
        HttpClient httpClient = HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
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
