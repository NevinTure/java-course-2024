package edu.java.bot.repositories;

import edu.java.bot.model.TgChat;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryChatRepository implements ChatRepository {

    Map<Long, TgChat> storage = new HashMap<>();

    @Override
    public void save(TgChat tgChat) {
        if (tgChat != null) {
            storage.put(tgChat.getId(), tgChat);
        }
    }

    @Override
    public Optional<TgChat> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
