package edu.java.scrapper.utils;

import edu.java.scrapper.configuration.ProducerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

public record KafkaProperties(
    @NestedConfigurationProperty
    ProducerProperties producer
) {
}
