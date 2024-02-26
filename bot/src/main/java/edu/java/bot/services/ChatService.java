package edu.java.bot.services;

import edu.java.bot.model.TgChat;
import java.util.List;
import java.util.Optional;

public interface ChatService {

    List<TgChat> getByIds(List<Long> ids);

    void save(TgChat tgChat);

    Optional<TgChat> getById(long id);

}
