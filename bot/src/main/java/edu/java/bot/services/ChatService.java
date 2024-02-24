package edu.java.bot.services;

import edu.java.bot.model.TgChat;
import java.util.List;

public interface ChatService {

    List<TgChat> getByIds(List<Long> ids);

    void save(TgChat tgChat);

}
