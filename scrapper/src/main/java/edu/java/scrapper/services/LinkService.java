package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LinkService {

    Link save(Link link);

    Optional<Link> getById(long id);

    boolean existsById(long id);

    void deleteById(long id);

    Optional<Link> findByUrl(URI uri);

    List<Link> findLinkByIds(List<Long> ids);

    Map<Link, UpdateType> mapIdsToLinksWithUpdateType(Map<Long, UpdateType> updatedLinkIds);

}
