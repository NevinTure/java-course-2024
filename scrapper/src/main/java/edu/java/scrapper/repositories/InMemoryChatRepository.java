package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryChatRepository implements ChatRepository {

    private final Map<Long, TgChat> storage = new HashMap<>();

    @Override
    public TgChat save(TgChat tgChat) {
        storage.put(tgChat.getId(), tgChat);
        return tgChat;
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
    public void deleteById(long id) {
        storage.remove(id);
    }
}
