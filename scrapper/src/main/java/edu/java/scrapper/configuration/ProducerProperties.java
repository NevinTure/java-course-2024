package edu.java.scrapper.configuration;

import java.time.Duration;

public record ProducerProperties(
    String bootStrapServers,
    String clientId,
    String acksMode,
    Duration deliveryTimeout,
    Integer lingerMs,
    Integer batchSize,
    Integer maxInFlightPerConnection,
    String topicName
) {
}
