package edu.java.scrapper.services.jpa;

import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.services.ChatService;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
public class JpaChatService implements ChatService {

    private final JpaChatRepository chatRepository;

    public JpaChatService(JpaChatRepository chatRepository) {
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
