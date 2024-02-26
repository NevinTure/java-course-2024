package edu.java.bot.clients;

import edu.java.bot.dtos.ApiErrorResponse;
import edu.java.bot.dtos.LinkUpdateRequest;

public interface BotApiClient {

    ApiErrorResponse sendUpdate(LinkUpdateRequest request);
}
