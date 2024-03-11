package edu.java.bot.services;

import edu.java.bot.clients.scrapper_api.ScrapperApiClient;
import edu.java.bot.model.Link;
import edu.java.bot.model.TgChat;
import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.models.dtos.TgChatDto;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ScrapperApiClient scrapperApiClient;
    private final ModelMapper mapper;

    public ChatServiceImpl(ScrapperApiClient scrapperApiClient, ModelMapper mapper) {
        this.scrapperApiClient = scrapperApiClient;
        this.mapper = mapper;
    }

    @Override
    public void register(long id) {
        scrapperApiClient.registerChat(id);
    }

    @Override
    public void unregister(long id) {
        scrapperApiClient.deleteChat(id);
    }

    @Override
    public Optional<TgChat> getChatById(long id) {
        Optional<TgChatDto> dto = scrapperApiClient.getChatById(id);
        return dto.map(tgChatDto -> mapper.map(tgChatDto, TgChat.class));
    }

    @Override
    public void updateChatById(long id, TgChat chat) {
        TgChatDto dto = mapper.map(chat, TgChatDto.class);
        scrapperApiClient.updateChatById(id, dto);
    }

    @Override
    public List<Link> getLinksById(long id) {
        return scrapperApiClient
            .getLinksById(id)
            .getLinks()
            .stream()
            .map(v -> mapper.map(v, Link.class))
            .toList();
    }

    @Override
    public Link addLinksByChatId(long id, Link link) {
        AddLinkRequest request = new AddLinkRequest(link.getUrl().toString());
        LinkResponse response = scrapperApiClient.addLinksByChatId(id, request);
        return mapper.map(response, Link.class);
    }

    @Override
    public Link deleteLinkByChatId(long id, Link link) {
        RemoveLinkRequest request = new RemoveLinkRequest(link.getUrl().toString());
        LinkResponse response = scrapperApiClient.deleteLinkByChatId(id, request);
        return mapper.map(response, Link.class);
    }
}
