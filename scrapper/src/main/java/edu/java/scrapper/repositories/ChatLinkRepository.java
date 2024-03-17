package edu.java.scrapper.repositories;

import edu.java.scrapper.model.Link;
import java.util.List;

public interface ChatLinkRepository {

    void addLink(long chatId, long linkId);

    void removeLink(long chatId, long linkId);

    List<Link> getLinksByChatId(long chatId);

    void deleteChatRelatedLinks(long chatId);

    boolean existsByChatAndLinkId(long chatId, long linkId);
}
