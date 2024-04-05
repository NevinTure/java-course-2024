package edu.java.scrapper.configuration;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.utils.KafkaProducerProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> producerFactory(ApplicationConfig appConfig) {
        KafkaProducerProperties kafka = appConfig.kafka();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootStrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, kafka.acksMode());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafka.clientId());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafka.batchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafka.lingerMs());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, (int) kafka.deliveryTimeout().toMillis());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafka.maxInFlightPerConnection());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate(ProducerFactory<String, LinkUpdateRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
