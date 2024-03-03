package edu.java.scrapper.clients.bot_api;

import edu.java.models.dtos.LinkUpdateRequest;

public interface BotApiClient {

    void sendUpdate(LinkUpdateRequest request);
}
