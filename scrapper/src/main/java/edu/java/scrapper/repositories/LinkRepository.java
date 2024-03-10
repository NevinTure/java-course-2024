package edu.java.scrapper.repositories;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    Integer save(Link link);

    Optional<Link> findById(long id);

    boolean existsById(long id);

    void deleteById(long id);

    Optional<Link> findByUrl(URI uri);

    List<Link> findLinkByIds(List<Long> ids);
}
