package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.model.TgChat;
import java.util.Optional;
import edu.java.scrapper.repositories.jdbc.JdbcChatRepository;
import edu.java.scrapper.services.ChatService;
import org.springframework.stereotype.Service;

public class JdbcChatService implements ChatService {

    private final JdbcChatRepository chatRepository;

    public JdbcChatService(JdbcChatRepository chatRepository) {
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
