package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;

public interface LinkUpdateSenderService {

    void sendUpdate(Link link, UpdateType updateType);
}
