package edu.java.controllers;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.model.TgChat;
import edu.java.services.ChatService;
import java.util.Optional;
import jakarta.validation.constraints.Min;
import jakarta.websocket.OnClose;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            return new ResponseEntity<>(tgChat.getLinkList(), HttpStatus.OK);
        } else {
            throw new ChatNotFoundException(id);
        }
    }
}
