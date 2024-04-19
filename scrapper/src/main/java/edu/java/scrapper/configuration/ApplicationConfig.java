package edu.java.scrapper.configuration;

import edu.java.models.utils.RetryPolicy;
import edu.java.scrapper.utils.AccessType;
import edu.java.scrapper.utils.KafkaProperties;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    String gitBaseUrl,
    String sofBaseUrl,
    String botApiBaseUrl,
    @DefaultValue("jdbc") AccessType databaseAccessType,
    @NestedConfigurationProperty
    RetryPolicy retryPolicy,
    @NestedConfigurationProperty
    KafkaProperties kafka,
    Boolean useQueue
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }
}
