package edu.java.bot.repositories;

import edu.java.bot.model.TgChat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final Map<Long, TgChat> storage = new HashMap<>();

    @Override
    public List<TgChat> getByIds(List<Long> ids) {
        List<TgChat> tgChatList = new ArrayList<>(ids.size());
        for (long id :ids) {
            if (storage.containsKey(id)) {
                tgChatList.add(storage.get(id));
            }
        }
        return tgChatList;
    }

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
