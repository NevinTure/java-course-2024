package edu.java.bot.services;

import edu.java.bot.model.LinkUpdate;

public interface LinkUpdateService {

    void update(LinkUpdate update);
    LinkUpdate getById(long id);
}
