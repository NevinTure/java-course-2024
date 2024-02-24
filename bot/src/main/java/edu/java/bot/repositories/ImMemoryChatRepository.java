package edu.java.bot.repositories;

import edu.java.bot.model.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ImMemoryChatRepository implements ChatRepository {

    private final Map<Long, Person> storage = new HashMap<>();

    @Override
    public List<Person> getByIds(List<Long> ids) {
        List<Person> personList = new ArrayList<>(ids.size());
        for (long id :ids) {
            if (storage.containsKey(id)) {
                personList.add(storage.get(id));
            }
        }
        return personList;
    }

    @Override
    public void save(Person person) {
        storage.put(person.getId(), person);
    }
}
