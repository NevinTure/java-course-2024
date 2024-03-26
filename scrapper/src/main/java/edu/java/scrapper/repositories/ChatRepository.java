package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.row_mappers.TgChatRowMapper;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    public TgChat save(TgChat tgChat);

    public Optional<TgChat> findById(long id);

    public boolean existsById(long id);

    public void deleteById(long id);
}
