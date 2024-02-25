package edu.java.bot.service;

import edu.java.bot.model.TgChat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import edu.java.bot.repositories.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;

    public ChatServiceImpl(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(TgChat tgChat) {
        repository.save(tgChat);
    }

    @Override
    public Optional<TgChat> getById(long id) {
        return repository.findById(id);
    }
}
