package edu.java.clients.bot_api;

import edu.java.dtos.bot_api.LinkUpdateRequest;

public interface BotApiClient {

    void sendUpdate(LinkUpdateRequest request);
}
