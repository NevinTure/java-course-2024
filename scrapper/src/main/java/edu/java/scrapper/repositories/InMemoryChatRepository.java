package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final Map<Long, TgChat> storage = new HashMap<>();

    @Override
    public void save(TgChat tgChat) {
        storage.put(tgChat.getId(), tgChat);
    }

    @Override
    public Optional<TgChat> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean existsById(long id) {
        return storage.containsKey(id);
    }

    @Override
    public long deleteById(long id) {
        return storage.remove(id) == null ? 0 : 1;
    }
}
