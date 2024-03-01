package edu.java.scrapper.clients.bot_api;

import edu.java.scrapper.dtos.bot_api.LinkUpdateRequest;

public interface BotApiClient {

    void sendUpdate(LinkUpdateRequest request);
}
