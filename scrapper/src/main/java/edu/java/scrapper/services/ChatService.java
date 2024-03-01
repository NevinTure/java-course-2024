package edu.java.scrapper.services;

import edu.java.scrapper.dtos.LinkResponse;
import edu.java.scrapper.dtos.ListLinksResponse;
import edu.java.scrapper.dtos.RemoveLinkRequest;
import edu.java.scrapper.dtos.AddLinkRequest;
import edu.java.scrapper.model.TgChat;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface ChatService {

    void save(TgChat tgChat);

    Optional<TgChat> getById(long id);

    boolean existsById(long id);

    void deleteById(long id);

    void register(long id);

    ResponseEntity<Object> checkedDeleteById(long id);

    ResponseEntity<ListLinksResponse> getLinksById(long id);

    ResponseEntity<LinkResponse> addLink(long id, AddLinkRequest addRequest);

    ResponseEntity<LinkResponse> removeLink(long id, RemoveLinkRequest removeRequest);
}
