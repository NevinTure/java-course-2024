package edu.java.clients;

import edu.java.dtos.AddLinkRequest;
import edu.java.dtos.LinkResponse;
import edu.java.dtos.ListLinksResponse;
import edu.java.dtos.RemoveLinkRequest;

public interface ScrapperApiClient {

    void registerChat(long id);

    void deleteChat(long id);

    ListLinksResponse getLinksById(long id);

    LinkResponse addLinksByChatId(long id, AddLinkRequest request);

    LinkResponse deleteLinkByChatId(long id, RemoveLinkRequest request);
}
