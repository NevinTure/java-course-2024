package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import edu.java.scrapper.utils.UpdateType;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LinkServiceImpl implements LinkService {
    private final LinkRepository repository;

    public LinkServiceImpl(LinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public Link save(Link link) {
        return repository.save(link);
    }

    @Override
    public Optional<Link> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Link> findByUrl(URI uri) {
        return repository.findByUrl(uri);
    }

    @Override
    public List<Link> findLinkByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return repository.findByIdIn(ids);
    }

    @Override
    public Map<Link, UpdateType> mapIdsToLinksWithUpdateType(Map<Long, UpdateType> updatedLinkIds) {
        Map<Link, UpdateType> updatedLinksMap = new HashMap<>(updatedLinkIds.size());
        List<Link> foundLinks = findLinkByIds(updatedLinkIds.keySet().stream().toList());
        for (Link link : foundLinks) {
            updatedLinksMap.put(link, updatedLinkIds.get(link.getId()));
        }
        return updatedLinksMap;
    }

    @Override
    public List<Long> findLinkFollowerIdsByLinkId(long linkId) {
        return repository.findLinkFollowerIdsByLinkId(linkId);
    }
}
