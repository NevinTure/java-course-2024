package edu.java.bot.service;

import edu.java.bot.model.Person;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class InMemoryChatService implements ChatService {

    Map<Long, Person> storage = new HashMap<>();

    @Override
    public void save(Person person) {
        if (person != null) {
            storage.put(person.getId(), person);
        }
    }

    @Override
    public Optional<Person> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
