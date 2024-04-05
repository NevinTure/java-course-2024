package edu.java.bot.services;

import edu.java.models.dtos.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdatesKafkaListener {

    private final LinkUpdateService updateService;

    public UpdatesKafkaListener(LinkUpdateService updateService) {
        this.updateService = updateService;
    }

    @KafkaListener(groupId = "updates-group",
        topics = "${app.kafka.consumer.topic-name}",
        containerFactory = "linkUpdateRequestContainerFactory")
    public void listenUpdates(LinkUpdateRequest request) {
        updateService.update(request);
    }
}
