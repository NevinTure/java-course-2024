package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface LinkService {

    void save(Link link);

    Optional<Link> getById(long id);

    boolean existsById(long id);

    void deleteById(long id);

    Optional<Link> findByUrl(URI uri);

    List<Link> findLinkByIds(List<Long> ids);

}
