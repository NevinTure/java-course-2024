package edu.java.bot.service;

import edu.java.bot.model.TgChat;
import java.util.Optional;

public interface ChatService {

    void save(TgChat tgChat);

    Optional<TgChat> getById(long id);
}
