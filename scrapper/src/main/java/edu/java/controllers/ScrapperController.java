package edu.java.controllers;

import edu.java.dtos.AddLinkRequest;
import edu.java.dtos.LinkResponse;
import edu.java.dtos.ListLinksResponse;
import edu.java.dtos.RemoveLinkRequest;
import edu.java.services.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    public ScrapperController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Object> registerChat(@PathVariable("id") @Min(0) long id) {
        chatService.register(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Object> deleteChat(@PathVariable("id") @Min(0) long id) {
        return chatService.checkedDeleteById(id);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinksByChatId(@RequestHeader("id") @Min(0) long id) {
        return chatService.getLinksById(id);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLinkByChatId(
        @RequestHeader("id") @Min(0) long id,
        @RequestBody @Valid AddLinkRequest addRequest
    ) {
        return chatService.addLink(id, addRequest);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLinkByChatId(
        @RequestHeader("id") @Min(0) long id,
        @RequestBody @Valid RemoveLinkRequest removeRequest
    ) {
        return chatService.removeLink(id, removeRequest);
    }
}
