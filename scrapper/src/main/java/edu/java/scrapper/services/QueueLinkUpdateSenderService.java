package edu.java.scrapper.services;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static edu.java.scrapper.utils.LinkUpdateRequestUtils.createRequest;

@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueLinkUpdateSenderService implements LinkUpdateSenderService {

    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final ChatLinkService chatLinkService;
    private final ApplicationConfig config;

    public QueueLinkUpdateSenderService(
        KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate,
        ChatLinkService chatLinkService,
        ApplicationConfig config
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.chatLinkService = chatLinkService;
        this.config = config;
    }

    @Override
    public void sendUpdate(Link link, UpdateType updateType) {
        LinkUpdateRequest request = createRequest(
            link,
            chatLinkService.findLinkFollowerIdsByLinkId(link.getId()),
            updateType);
        if (request.getTgChatIds().isEmpty()) {
            return;
        }
        kafkaTemplate.send(config.kafka().producer().topicName(), request);
    }
}
