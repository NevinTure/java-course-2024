package edu.java.bot.clients.scrapper_api;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;

public interface ScrapperApiClient {

    void registerChat(long id);

    void deleteChat(long id);

    ListLinksResponse getLinksById(long id);

    LinkResponse addLinksByChatId(long id, AddLinkRequest request);

    LinkResponse deleteLinkByChatId(long id, RemoveLinkRequest request);
}
