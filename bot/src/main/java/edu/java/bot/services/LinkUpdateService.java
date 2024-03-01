package edu.java.bot.services;

import edu.java.bot.model.LinkUpdate;
import edu.java.models.dtos.LinkUpdateRequest;
import java.util.Optional;

public interface LinkUpdateService {

    void update(LinkUpdateRequest update);

    Optional<LinkUpdate> getById(long id);
}
