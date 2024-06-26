package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.ChatRepository;
import edu.java.scrapper.services.ChatService;
import java.util.Optional;

public class JdbcChatService implements ChatService {

    private final ChatRepository chatRepository;

    public JdbcChatService(ChatRepository chatRepository) {
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
