package edu.java.bot.repositories;

import edu.java.bot.model.TgChat;
import java.util.Optional;

public interface ChatRepository {

    void save(TgChat tgChat);

    Optional<TgChat> findById(long id);
}
