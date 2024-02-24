package edu.java.bot.services;

import edu.java.bot.model.TgChat;
import edu.java.bot.repositories.ChatRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;

    public ChatServiceImpl(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TgChat> getByIds(List<Long> ids) {
        return repository.getByIds(ids);
    }

    @Override
    public void save(TgChat tgChat) {
        repository.save(tgChat);
    }
}
