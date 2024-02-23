package edu.java.bot.repositories;

import edu.java.bot.model.LinkUpdate;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class InMemoryLinkUpdateRepository implements LinkUpdateRepository {

    Map<Long, LinkUpdate> storage;

    @Override
    public void save(LinkUpdate update) {
        storage.put(update.getId(), update);
    }
}
