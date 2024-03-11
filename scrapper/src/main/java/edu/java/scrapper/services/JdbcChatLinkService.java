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
import edu.java.scrapper.repositories.ChatLinkRepository;
import edu.java.scrapper.repositories.LinkRepository;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JdbcChatLinkService implements ChatLinkService {

    private final ChatService chatService;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final RecognizeLinkService recognizeService;
    private final ModelMapper mapper;

    public JdbcChatLinkService(
        ChatService chatService, LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        @Lazy RecognizeLinkService recognizeService, ModelMapper mapper
    ) {
        this.chatService = chatService;
        this.linkRepository = linkRepository;
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
        return chatLinkRepository.findLinkFollowerIdsByLinkId(linkId);
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
            link.setId(linkId);
            recognizeService.recognize(link);

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
