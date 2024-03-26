package edu.java.scrapper.controllers;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.models.dtos.TgChatDto;
import edu.java.scrapper.services.ChatLinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api")
public class ScrapperController {

    private final ChatLinkService chatLinkService;

    public ScrapperController(ChatLinkService chatLinkService) {
        this.chatLinkService = chatLinkService;
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Object> registerChat(@PathVariable("id") @Min(0) long id) {
        chatLinkService.register(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Object> deleteChat(@PathVariable("id") @Min(0) long id) {
        return chatLinkService.unregister(id);
    }

    @GetMapping("/tg_chat/{id}")
    public ResponseEntity<TgChatDto> getChatById(@PathVariable("id") @Min(0) long id) {
        return chatLinkService.getChatById(id);
    }

    @PutMapping("/tg_chat/{id}")
    public ResponseEntity<Object> updateChatById(@PathVariable("id") @Min(0) long id, @RequestBody TgChatDto dto) {
        return chatLinkService.updateChatById(id, dto);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinksByChatId(@RequestHeader("id") @Min(0) long id) {
        return chatLinkService.getLinksById(id);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLinkByChatId(
        @RequestHeader("id") @Min(0) long id,
        @RequestBody @Valid AddLinkRequest addRequest
    ) {
        return chatLinkService.addLink(id, addRequest);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLinkByChatId(
        @RequestHeader("id") @Min(0) long id,
        @RequestBody @Valid RemoveLinkRequest removeRequest
    ) {
        return chatLinkService.removeLink(id, removeRequest);
    }
}
