package edu.java.bot.controllers;

import edu.java.bot.dtos.LinkUpdateRequest;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.services.LinkUpdateService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api")
public class BotController {

    private final LinkUpdateService linkUpdateService;
    private final ModelMapper mapper;

    public BotController(LinkUpdateService linkUpdateService, ModelMapper mapper) {
        this.linkUpdateService = linkUpdateService;
        this.mapper = mapper;
    }

    @PostMapping("/updates")
    public ResponseEntity<Object> sendUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        LinkUpdate update = mapper.map(linkUpdateRequest, LinkUpdate.class);
        System.out.println(update);
        linkUpdateService.update(update);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
