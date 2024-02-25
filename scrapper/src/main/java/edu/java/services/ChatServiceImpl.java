package edu.java.services;

import edu.java.exceptions.ChatNotFoundException;
import edu.java.exceptions.LinkAlreadyTrackedException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.model.Link;
import edu.java.model.TgChat;
import edu.java.repositories.ChatRepository;
import java.net.URI;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;

    public ChatServiceImpl(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(TgChat tgChat) {
        repository.save(tgChat);
    }

    @Override
    public Optional<TgChat> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public void addLink(long id, Link link) {
        Optional<TgChat> chatOptional = getById(id);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndAdd(chat, link);
            save(chat);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @Override
    public void removeLink(long id, Link link) {
        Optional<TgChat> chatOptional = getById(id);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndRemove(chat, link);
            save(chat);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    private Optional<Link> findLinkByUrl(TgChat chat, URI url) {
        for (Link link : chat.getLinkList()) {
            if (link.getUrl().equals(url)) {
                return Optional.of(link);
            }
        }
        return Optional.empty();
    }

    private void checkLinkAndAdd(TgChat chat, Link link) {
        Optional<Link> foundLink = findLinkByUrl(chat, link.getUrl());
        if (foundLink.isPresent()) {
            throw new LinkAlreadyTrackedException(chat.getId(), link.getUrl());
        }
        chat.getLinkList().add(link);
    }

    private void checkLinkAndRemove(TgChat chat, Link link) {
        Optional<Link> foundLink = findLinkByUrl(chat, link.getUrl());
        if (foundLink.isPresent()) {
            link.setId(foundLink.get().getId());
            chat.getLinkList().remove(foundLink.get());
        } else {
            throw new LinkNotFoundException(chat.getId(), link.getUrl());
        }
    }
}
