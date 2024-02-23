package edu.java.bot.services;

import edu.java.bot.exceptions.ChatNotFoundException;
import edu.java.bot.model.Person;
import edu.java.bot.repositories.ChatRepository;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;

    public ChatServiceImpl(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Person> getByIds(List<Long> ids) {
        return repository.getByIds(ids);
    }
}
