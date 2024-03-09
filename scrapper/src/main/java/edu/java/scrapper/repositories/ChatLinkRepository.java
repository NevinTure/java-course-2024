package edu.java.scrapper.repositories;

import edu.java.scrapper.model.Link;

public interface ChatLinkRepository {

    void addLink(long chatId, long linkId);

    void removeLink(long chatId, long linkId);

    void deleteChatRelatedLinks(long chatId);

    boolean existsByChatAndLinkId(long chatId, long linkId);
}
