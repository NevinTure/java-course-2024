package edu.java.bot.services;

import edu.java.models.dtos.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdatesKafkaListener {

    private final LinkUpdateService updateService;

    public UpdatesKafkaListener(LinkUpdateService updateService) {
        this.updateService = updateService;
    }

    @RetryableTopic(attempts = "1", dltTopicSuffix = "_dlq", dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(groupId = "updates-group",
        topics = "${app.kafka.consumer.topic-name}",
        containerFactory = "linkUpdateRequestContainerFactory"
    )
    public void listenUpdates(LinkUpdateRequest request) {
        updateService.update(request);
    }

    @DltHandler
    public void handleDltLinkUpdateRequest(
        LinkUpdateRequest request,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Failed to process message {} from topic: {}", request.toString(), topic);
    }
}
