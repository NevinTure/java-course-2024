package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.utils.UpdateType;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;

    public JdbcLinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Link save(Link link) {
        return linkRepository.save(link);
    }

    @Override
    public Optional<Link> getById(long id) {
        return linkRepository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return linkRepository.existsById(id);
    }

    @Override
    public void deleteById(long id) {
        linkRepository.deleteById(id);
    }

    @Override
    public Optional<Link> findByUrl(URI uri) {
        return linkRepository.findByUrl(uri);
    }

    @Override
    public List<Link> findLinkByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return linkRepository.findByIdIn(ids);
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
        return linkRepository.findLinkFollowerIdsByLinkId(linkId);
    }
}
