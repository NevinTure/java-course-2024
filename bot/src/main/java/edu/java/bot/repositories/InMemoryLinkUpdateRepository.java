package edu.java.bot.repositories;

import edu.java.bot.model.LinkUpdate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLinkUpdateRepository implements LinkUpdateRepository {

    private final Map<Long, LinkUpdate> storage = new HashMap<>();

    @Override
    public void save(LinkUpdate update) {
        storage.put(update.getId(), update);
    }

    @Override
    public Optional<LinkUpdate> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
