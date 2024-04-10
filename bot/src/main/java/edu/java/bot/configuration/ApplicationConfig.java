package edu.java.bot.configuration;

import edu.java.models.utils.RetryPolicy;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    String scrapperApiBaseUrl,
    @NestedConfigurationProperty
    RetryPolicy retryPolicy
) {
}
