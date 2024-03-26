package edu.java.bot.clients.scrapper_api;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.models.dtos.TgChatDto;
import java.util.Optional;

public interface ScrapperApiClient {

    void registerChat(long id);

    void deleteChat(long id);

    Optional<TgChatDto> getChatById(long id);

    void updateChatById(long id, TgChatDto dto);

    ListLinksResponse getLinksById(long id);

    LinkResponse addLinksByChatId(long id, AddLinkRequest request);

    LinkResponse deleteLinkByChatId(long id, RemoveLinkRequest request);
}
