package edu.java.bot.repositories;

import edu.java.bot.model.LinkUpdate;
import java.util.Optional;

public interface LinkUpdateRepository {

    void save(LinkUpdate update);

    Optional<LinkUpdate> findById(long id);
}
