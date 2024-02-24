package edu.java.repositories;

import edu.java.model.TgChat;
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
}
