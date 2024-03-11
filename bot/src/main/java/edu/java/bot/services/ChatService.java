package edu.java.bot.services;

import edu.java.bot.model.Link;
import edu.java.bot.model.TgChat;
import java.util.List;
import java.util.Optional;

public interface ChatService {

    void register(long id);

    void unregister(long id);

    Optional<TgChat> getChatById(long id);

    void updateChatById(long id, TgChat chat);

    List<Link> getLinksById(long id);

    Link addLinksByChatId(long id, Link link);

    Link deleteLinkByChatId(long id, Link link);

}
