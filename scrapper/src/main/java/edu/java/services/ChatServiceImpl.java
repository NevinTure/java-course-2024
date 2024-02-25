package edu.java.services;

import edu.java.model.TgChat;
import edu.java.repositories.ChatRepository;
import java.util.Optional;
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

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
