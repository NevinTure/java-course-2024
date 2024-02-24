package edu.java.bot.repositories;

import edu.java.bot.model.Person;
import java.util.List;

public interface ChatRepository {

    List<Person> getByIds(List<Long> ids);
    void save(Person person);
}
