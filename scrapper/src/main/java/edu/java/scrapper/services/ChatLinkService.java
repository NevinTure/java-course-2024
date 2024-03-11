package edu.java.scrapper.services;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ChatLinkService {

    void register(long chatId);

    ResponseEntity<Object> unregister(long chatId);

    ResponseEntity<ListLinksResponse> getLinksById(long id);

    ResponseEntity<LinkResponse> addLink(long chatId, AddLinkRequest request);

    ResponseEntity<LinkResponse> removeLink(long chatId, RemoveLinkRequest request);

    List<Long> findLinkFollowerIdsByLinkId(long linkId);
}
