package edu.java.controllers;

import edu.java.dtos.AddLinkRequest;
import edu.java.dtos.LinkResponse;
import edu.java.dtos.ListLinksResponse;
import edu.java.dtos.RemoveLinkRequest;
import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.model.Link;
import edu.java.model.TgChat;
import edu.java.services.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api")
public class ScrapperController {

    private final ChatService chatService;
    private final ModelMapper mapper;

    public ScrapperController(ChatService chatService, ModelMapper mapper) {
        this.chatService = chatService;
        this.mapper = mapper;
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Object> registerChat(@PathVariable("id") @Min(0) long id) {
        Optional<TgChat> optionalTgChat = chatService.getById(id);
        if (optionalTgChat.isPresent()) {
            throw new ChatAlreadyRegisteredException(id);
        }
        chatService.save(new TgChat(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Object> deleteChat(@PathVariable("id") @Min(0) long id) {
        if (chatService.existsById(id)) {
            chatService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @GetMapping("/links")
    public ResponseEntity<Object> getLinksById(@RequestHeader("id") @Min(0) long id) {
        Optional<TgChat> optionalChat = chatService.getById(id);
        if (optionalChat.isPresent()) {
            TgChat tgChat = optionalChat.get();
            ListLinksResponse listLinksResponse = mapper.map(tgChat, ListLinksResponse.class);
            return new ResponseEntity<>(listLinksResponse, HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }

    @PostMapping("/links")
    public ResponseEntity<Object> addLinkById(
        @RequestHeader("id") @Min(0) long id,
        @RequestBody @Valid AddLinkRequest addRequest
    ) {
        Link link = mapper.map(addRequest, Link.class);
        chatService.addLink(id, link);
        LinkResponse response = mapper.map(link, LinkResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<Object> deleteLinkById(
        @RequestHeader("id") @Min(0) long id,
        @RequestBody @Valid RemoveLinkRequest removeRequest
    ) {
        Link link = mapper.map(removeRequest, Link.class);
        chatService.removeLink(id, link);
        LinkResponse response = mapper.map(link, LinkResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
