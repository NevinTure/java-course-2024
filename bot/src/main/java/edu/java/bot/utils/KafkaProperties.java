package edu.java.bot.utils;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public record KafkaProperties(
    @NestedConfigurationProperty
    ConsumerProperties consumer,
    @NestedConfigurationProperty
    ProducerProperties producer
) {
}
