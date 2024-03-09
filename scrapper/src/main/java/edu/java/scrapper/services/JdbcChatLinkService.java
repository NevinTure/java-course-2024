package edu.java.scrapper.services;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.scrapper.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.exceptions.ChatNotFoundException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.LinkNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.ChatLinkRepository;
import edu.java.scrapper.repositories.ChatRepository;
import edu.java.scrapper.repositories.LinkRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class JdbcChatLinkService implements ChatLinkService {

    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final ModelMapper mapper;

    public JdbcChatLinkService(
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        ModelMapper mapper
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.mapper = mapper;
    }

    @Override
    public void register(long chatId) {
        if (chatRepository.existsById(chatId)) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        chatRepository.save(new TgChat(chatId));
    }

    @Override
    public ResponseEntity<Object> unregister(long chatId) {
        if (chatRepository.existsById(chatId)) {
            chatRepository.deleteById(chatId);
            chatLinkRepository.deleteChatRelatedLinks(chatId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(chatId);
        }
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(long chatId, AddLinkRequest addRequest) {
        Link link = mapper.map(addRequest, Link.class);
        Optional<TgChat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndAdd(chat, link);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(chatId);
        }
    }

    @Override
    public ResponseEntity<LinkResponse> removeLink(long chatId, RemoveLinkRequest removeRequest) {
        Link link = mapper.map(removeRequest, Link.class);
        Optional<TgChat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndRemove(chat, link);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(chatId);
        }
    }

    private void checkLinkAndAdd(TgChat chat, Link link) {
        if (chatLinkRepository.existsByChatAndLinkId(chat.getId(), link.getId())) {
            throw new LinkAlreadyTrackedException(chat.getId(), link.getUrl());
        }
        Optional<Link> foundLinkOp = linkRepository.findByUrl(link.getUrl());
        if (foundLinkOp.isPresent()) {
            Link foundLink = foundLinkOp.get();
            chatLinkRepository.addLink(chat.getId(), foundLink.getId());
        } else {
            Integer linkId = linkRepository.save(link);
            chatLinkRepository.addLink(chat.getId(), linkId);
            //TODO добавить в репозиторий/вопросы
        }
    }

    private void checkLinkAndRemove(TgChat chat, Link link) {
        if (!chatLinkRepository.existsByChatAndLinkId(chat.getId(), link.getId())) {
            throw new LinkNotFoundException(chat.getId(), link.getUrl());
        }
        Optional<Link> foundLinkOp = linkRepository.findByUrl(link.getUrl());
        if (foundLinkOp.isPresent()) {
            Link foundLink = foundLinkOp.get();
            chatLinkRepository.removeLink(chat.getId(), foundLink.getId());
        } else {
            throw new LinkNotFoundException(chat.getId(), link.getUrl());
        }
    }
}
