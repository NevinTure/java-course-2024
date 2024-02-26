package edu.java.bot.repositories;

import edu.java.bot.model.TgChat;
import java.util.Optional;
import java.util.List;

public interface ChatRepository {

    List<TgChat> getByIds(List<Long> ids);

    void save(TgChat tgChat);
  
    Optional<TgChat> findById(long id);
  
}
