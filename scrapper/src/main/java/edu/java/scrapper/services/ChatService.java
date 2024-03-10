package edu.java.scrapper.services;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.scrapper.model.TgChat;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

public interface ChatService {

    void save(TgChat tgChat);

    Optional<TgChat> getById(long id);

    boolean existsById(long id);

    void deleteById(long id);
}
