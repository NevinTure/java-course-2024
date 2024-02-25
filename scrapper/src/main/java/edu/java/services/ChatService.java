package edu.java.services;

import edu.java.model.TgChat;
import java.util.Optional;

public interface ChatService {

    void save(TgChat tgChat);

    Optional<TgChat> getById(long id);

    boolean existsById(long id);

    void deleteById(long id);
}
