package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
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
    public void save(Link link) {
        linkRepository.save(link);
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
        return linkRepository.findLinkByIds(ids);
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
}
