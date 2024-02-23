package edu.java.bot.services;

import edu.java.bot.model.Person;
import java.util.List;
import java.util.Set;

public interface ChatService {

    List<Person> getByIds(List<Long> ids);

}
