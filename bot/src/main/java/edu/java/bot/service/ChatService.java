package edu.java.bot.service;

import edu.java.bot.model.Person;
import java.util.Optional;

public interface ChatService {

    void save(Person person);

    Optional<Person> getById(long id);
}
