package edu.java.bot.services;

import edu.java.bot.model.LinkUpdate;
import java.util.Optional;

public interface LinkUpdateService {

    void update(LinkUpdate update);

    Optional<LinkUpdate> getById(long id);
}
