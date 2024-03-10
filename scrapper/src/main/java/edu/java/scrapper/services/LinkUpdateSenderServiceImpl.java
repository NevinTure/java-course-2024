package edu.java.scrapper.services;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.clients.bot_api.BotApiClient;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdateSenderServiceImpl implements LinkUpdateSenderService {

    private final BotApiClient botApiClient;
    private final ChatLinkService chatLinkService;

    public LinkUpdateSenderServiceImpl(BotApiClient botApiClient, ChatLinkService chatLinkService) {
        this.botApiClient = botApiClient;
        this.chatLinkService = chatLinkService;
    }

    @Override
    public void sendUpdate(Link link, UpdateType updateType) {
        LinkUpdateRequest request = createRequest(link, updateType);
        botApiClient.sendUpdate(request);
    }

    private LinkUpdateRequest createRequest(Link link, UpdateType updateType) {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.setId(link.getId());
        request.setUrl(link.getUrl().toString());
        request.setTgChatIds(chatLinkService.findLinkFollowerIdsByLinkId(link.getId()));
        String description = switch (updateType) {
            case UPDATE -> "Обновление";
            case ANSWER -> "Новый ответ";
            case PUSH -> "Новый коммит";
            default -> "";
        };
        request.setDescription(description);
        return request;
    }
}
