package edu.java.scrapper.services.jdbc;

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
import edu.java.scrapper.repositories.ChatLinkRepository;
import edu.java.scrapper.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.RecognizeLinkService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcChatLinkService implements ChatLinkService {

    private final ChatService chatService;
    private final LinkService linkService;
    private final ChatLinkRepository chatLinkRepository;
    private final RecognizeLinkService recognizeService;
    private final ModelMapper mapper;

    public JdbcChatLinkService(
        ChatService chatService,
        LinkService linkService,
        ChatLinkRepository chatLinkRepository,
        @Lazy RecognizeLinkService recognizeService,
        ModelMapper mapper
    ) {
        this.chatService = chatService;
        this.linkService = linkService;
        this.chatLinkRepository = chatLinkRepository;
        this.recognizeService = recognizeService;
        this.mapper = mapper;
    }

    @Override
    public void register(long chatId) {
        if (chatService.existsById(chatId)) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        chatService.save(new TgChat(chatId));
    }

    @Override
    public ResponseEntity<Object> unregister(long chatId) {
        if (chatService.existsById(chatId)) {
            chatService.deleteById(chatId);
            chatLinkRepository.deleteChatRelatedLinks(chatId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(chatId);
        }
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinksById(long id) {
        if (chatService.existsById(id)) {
            List<Link> links = chatLinkRepository.getLinksByChatId(id);
            ListLinksResponse listLinksResponse
                = new ListLinksResponse(links.stream().map(v -> mapper.map(v, LinkResponse.class)).toList());
            return new ResponseEntity<>(listLinksResponse, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(long chatId, AddLinkRequest addRequest) {
        Link link = mapper.map(addRequest, Link.class);
        Optional<TgChat> chatOptional = chatService.getById(chatId);
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
        Optional<TgChat> chatOptional = chatService.getById(chatId);
        if (chatOptional.isPresent()) {
            TgChat chat = chatOptional.get();
            checkLinkAndRemove(chat, link);
            LinkResponse response = mapper.map(link, LinkResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(chatId);
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

    private void checkLinkAndAdd(TgChat chat, Link link) {
        Optional<Link> foundLinkOp = linkService.findByUrl(link.getUrl());
        if (foundLinkOp.isPresent()) {
            Link foundLink = foundLinkOp.get();
            if (chatLinkRepository.existsByChatAndLinkId(chat.getId(), foundLink.getId())) {
                throw new LinkAlreadyTrackedException(chat.getId(), link.getUrl());
            }
            chatLinkRepository.addLink(chat.getId(), foundLink.getId());
        } else {
            linkService.save(link);
            chatLinkRepository.addLink(chat.getId(), link.getId());
            recognizeService.recognize(link);

        }
    }

    private void checkLinkAndRemove(TgChat chat, Link link) {
        Optional<Link> foundLinkOp = linkService.findByUrl(link.getUrl());
        if (foundLinkOp.isPresent()) {
            Link foundLink = foundLinkOp.get();
            if (!chatLinkRepository.existsByChatAndLinkId(chat.getId(), foundLink.getId())) {
                throw new LinkNotFoundException(chat.getId(), link.getUrl());
            }
            chatLinkRepository.removeLink(chat.getId(), foundLink.getId());
        } else {
            throw new LinkNotFoundException(chat.getId(), link.getUrl());
        }
    }
}
