package edu.java.bot.clients.scrapper_api;

import edu.java.bot.dtos.scrapper_api.AddLinkRequest;
import edu.java.bot.dtos.scrapper_api.LinkResponse;
import edu.java.bot.dtos.scrapper_api.ListLinksResponse;
import edu.java.bot.dtos.scrapper_api.RemoveLinkRequest;

public interface ScrapperApiClient {

    void registerChat(long id);

    void deleteChat(long id);

    ListLinksResponse getLinksById(long id);

    LinkResponse addLinksByChatId(long id, AddLinkRequest request);

    LinkResponse deleteLinkByChatId(long id, RemoveLinkRequest request);
}
