package edu.java.scrapper.repositories;

import edu.java.scrapper.model.Link;
import java.util.List;

public interface ChatLinkRepository {

    public void addLink(long chatId, long linkId);

    public void removeLink(long chatId, long linkId);

    public List<Link> getLinksByChatId(long chatId);

    public void deleteChatRelatedLinks(long chatId);

    public boolean existsByChatAndLinkId(long chatId, long linkId);
}
