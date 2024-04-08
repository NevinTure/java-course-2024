package edu.java.scrapper.services;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.clients.bot_api.BotApiClient;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import static edu.java.scrapper.utils.LinkUpdateRequestUtils.createRequest;

@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpLinkUpdateSenderService implements LinkUpdateSenderService {

    private final BotApiClient botApiClient;
    private final ChatLinkService chatLinkService;

    public HttpLinkUpdateSenderService(BotApiClient botApiClient, ChatLinkService chatLinkService) {
        this.botApiClient = botApiClient;
        this.chatLinkService = chatLinkService;
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
        botApiClient.sendUpdate(request);
    }
}
