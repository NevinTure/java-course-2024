package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChat;
import java.util.Optional;

public interface ChatRepository {

    TgChat save(TgChat tgChat);

    Optional<TgChat> findById(long id);

    boolean existsById(long id);

    void deleteById(long id);
}
