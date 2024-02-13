package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    private final ApplicationConfig appConfig;

    public BotConfig(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Bean
    public TelegramBot bot() {
        return new TelegramBot(appConfig.telegramToken());
    }
}
