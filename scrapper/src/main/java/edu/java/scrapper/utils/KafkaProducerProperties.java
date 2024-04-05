package edu.java.scrapper.utils;

import java.time.Duration;

public record KafkaProducerProperties(
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
