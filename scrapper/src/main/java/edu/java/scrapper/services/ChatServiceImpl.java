package edu.java.scrapper.services;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.scrapper.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.exceptions.ChatNotFoundException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.LinkNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.ChatRepository;
import java.net.URI;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;
    private final ModelMapper mapper;

    public ChatServiceImpl(ChatRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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

    public void register(long id) {
        if (existsById(id)) {
            throw new ChatAlreadyRegisteredException(id);
        }
        save(new TgChat(id));
    }

    public ResponseEntity<Object> checkedDeleteById(long id) {
        if (existsById(id)) {
            deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    public ResponseEntity<ListLinksResponse> getLinksById(long id) {
        Optional<TgChat> optionalChat = getById(id);
        if (optionalChat.isPresent()) {
            TgChat tgChat = optionalChat.get();
            ListLinksResponse listLinksResponse = mapper.map(tgChat, ListLinksResponse.class);
            return new ResponseEntity<>(listLinksResponse, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    public ResponseEntity<LinkResponse> addLink(long id, AddLinkRequest addRequest) {
        Link link = mapper.map(addRequest, Link.class);
        Optional<TgChat> chatOptional = getById(id);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndAdd(chat, link);
            save(chat);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    public ResponseEntity<LinkResponse> removeLink(long id, RemoveLinkRequest removeRequest) {
        Link link = mapper.map(removeRequest, Link.class);
        Optional<TgChat> chatOptional = getById(id);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndRemove(chat, link);
            save(chat);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
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
