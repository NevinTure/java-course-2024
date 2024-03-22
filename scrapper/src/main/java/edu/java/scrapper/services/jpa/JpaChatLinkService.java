package edu.java.scrapper.services.jpa;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.models.dtos.TgChatDto;
import edu.java.scrapper.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.exceptions.ChatNotFoundException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.LinkNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.RecognizeLinkService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JpaChatLinkService implements ChatLinkService {

    private final ChatService chatService;
    private final LinkService linkService;
    private final RecognizeLinkService recognizeService;
    private final ModelMapper mapper;

    public JpaChatLinkService(ChatService chatService, LinkService linkService,
        RecognizeLinkService recognizeService,
        ModelMapper mapper) {
        this.chatService = chatService;
        this.linkService = linkService;
        this.recognizeService = recognizeService;
        this.mapper = mapper;
    }

    public void register(long id) {
        if (chatService.existsById(id)) {
            throw new ChatAlreadyRegisteredException(id);
        }
        chatService.save(new TgChat(id));
    }

    @Override
    public ResponseEntity<Object> unregister(long chatId) {
        if (chatService.existsById(chatId)) {
            chatService.deleteById(chatId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(chatId);
        }
    }

    public ResponseEntity<ListLinksResponse> getLinksById(long id) {
        Optional<TgChat> optionalChat = chatService.getById(id);
        if (optionalChat.isPresent()) {
            TgChat tgChat = optionalChat.get();
            ListLinksResponse listLinksResponse = mapper.map(tgChat, ListLinksResponse.class);
            return new ResponseEntity<>(listLinksResponse, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<TgChatDto> getChatById(long id) {
        Optional<TgChat> foundChat = chatService.getById(id);
        if (foundChat.isPresent()) {
            TgChatDto dto = mapper.map(foundChat.get(), TgChatDto.class);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<Object> updateChatById(long id, TgChatDto dto) {
        if (chatService.existsById(id)) {
            TgChat chat = mapper.map(dto, TgChat.class);
            chat.setId(id);
            chatService.save(chat);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(long id, AddLinkRequest addRequest) {
        Link link = mapper.map(addRequest, Link.class);
        Optional<TgChat> chatOptional = chatService.getById(id);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndAdd(chat, link);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    public ResponseEntity<LinkResponse> removeLink(long id, RemoveLinkRequest removeRequest) {
        Link link = mapper.map(removeRequest, Link.class);
        Optional<TgChat> chatOptional = chatService.getById(id);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndRemove(chat, link);
            chatService.save(chat);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @Override
    public List<Long> findLinkFollowerIdsByLinkId(long linkId) {
        if (linkService.existsById(linkId)) {
            return linkService.findLinkFollowerIdsByLinkId(linkId);
        } else {
            throw new LinkNotFoundException(linkId, URI.create(""));
        }
    }

    private void checkLinkAndAdd(TgChat chat, Link link) {
        Optional<Link> foundLinkOp = linkService.findByUrl(link.getUrl());
        if (foundLinkOp.isPresent()) {
            Link foundLink = foundLinkOp.get();
            if (chat.getLinkList().contains(foundLink)) {
                throw new LinkAlreadyTrackedException(chat.getId(), link.getUrl());
            }
            chat.getLinkList().add(foundLink);
            chatService.save(chat);
        } else {
            linkService.save(link);
            chat.getLinkList().add(link);
            recognizeService.recognize(link);
        }
    }

    private void checkLinkAndRemove(TgChat chat, Link link) {
        Optional<Link> foundLinkOp = linkService.findByUrl(link.getUrl());
        if (foundLinkOp.isPresent()) {
            Link foundLink = foundLinkOp.get();
            if (chat.getLinkList().contains(foundLink)) {
                throw new LinkNotFoundException(chat.getId(), link.getUrl());
            }
            chat.getLinkList().add(foundLink);
            chatService.save(chat);
        } else {
            throw new LinkNotFoundException(chat.getId(), link.getUrl());
        }
    }
}
