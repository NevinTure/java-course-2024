package edu.java.bot.services;

import edu.java.bot.exceptions.ChatNotFoundException;
import edu.java.bot.exceptions.UrlNotFoundException;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.model.TgChat;
import edu.java.bot.repositories.LinkUpdateRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdateServiceImpl implements LinkUpdateService {

    private final ChatService chatService;
    private final LinkUpdateRepository repository;

    public LinkUpdateServiceImpl(ChatService chatService, LinkUpdateRepository repository) {
        this.chatService = chatService;
        this.repository = repository;
    }

    @Override
    public void update(LinkUpdate update) {
        List<TgChat> tgChatList = chatService.getByIds(update.getTgChatIds());
        checkChatsById(new ArrayList<>(update.getTgChatIds()), tgChatList);
        checkUrlByChats(update.getUrl(), tgChatList);
        repository.save(update);
    }

    @Override
    public LinkUpdate getById(long id) {
        return repository.findById(id).orElse(null);
    }

    private void checkChatsById(List<Long> ids, List<TgChat> tgChatList) {
        Set<Long> idSet = tgChatList.stream().map(TgChat::getId).collect(Collectors.toSet());
        idSet.forEach(ids::remove);
        if (!ids.isEmpty()) {
            throw new ChatNotFoundException(ids);
        }
    }

    private void checkUrlByChats(String url, List<TgChat> tgChatList) {
        for (TgChat tgChat : tgChatList) {
            if (!tgChat.getLinkList().contains(url)) {
                throw new UrlNotFoundException(url, tgChat);
            }
        }
    }
}
