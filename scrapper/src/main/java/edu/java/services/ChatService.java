package edu.java.services;

import edu.java.dtos.AddLinkRequest;
import edu.java.dtos.LinkResponse;
import edu.java.dtos.ListLinksResponse;
import edu.java.dtos.RemoveLinkRequest;
import edu.java.model.TgChat;
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
