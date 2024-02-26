package edu.java.bot.clients;

import edu.java.bot.dtos.LinkUpdateRequest;

public interface BotApiClient {

    void sendUpdate(LinkUpdateRequest request);
}
