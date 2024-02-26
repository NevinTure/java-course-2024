package edu.java.bot.repositories;

import edu.java.bot.model.TgChat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    List<TgChat> getByIds(List<Long> ids);

    void save(TgChat tgChat);

    Optional<TgChat> findById(long id);

}
