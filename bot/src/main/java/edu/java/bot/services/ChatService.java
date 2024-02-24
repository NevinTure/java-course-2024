package edu.java.bot.services;

import edu.java.bot.model.Person;
import java.util.List;

public interface ChatService {

    List<Person> getByIds(List<Long> ids);

    void save(Person person);

}
