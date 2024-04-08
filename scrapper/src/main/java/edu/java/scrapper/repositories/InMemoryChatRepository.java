package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryChatRepository {

    private final Map<Long, TgChat> storage = new HashMap<>();

    public TgChat save(TgChat tgChat) {
        storage.put(tgChat.getId(), tgChat);
        return tgChat;
    }

    public Optional<TgChat> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public boolean existsById(long id) {
        return storage.containsKey(id);
    }

    public void deleteById(long id) {
        storage.remove(id);
    }
}
