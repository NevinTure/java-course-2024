package edu.java.scrapper.services;

import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.ChatRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void save(TgChat tgChat) {
        chatRepository.save(tgChat);
    }

    @Override
    public Optional<TgChat> getById(long id) {
        return chatRepository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return chatRepository.existsById(id);
    }

    @Override
    public void deleteById(long id) {
        chatRepository.deleteById(id);
    }
}
