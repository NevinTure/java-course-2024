package edu.java.controllers;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.model.TgChat;
import edu.java.services.ChatService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScrapperController {

    private final ChatService chatService;

    public ScrapperController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Object> registerChat(@PathVariable("id") long id) {
        Optional<TgChat> optionalTgChat = chatService.getById(id);
        if (optionalTgChat.isPresent()) {
            throw new ChatAlreadyRegisteredException(id);
        }
        chatService.save(new TgChat(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
