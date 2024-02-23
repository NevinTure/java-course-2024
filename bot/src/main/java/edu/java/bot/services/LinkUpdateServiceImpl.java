package edu.java.bot.services;

import edu.java.bot.exceptions.ChatNotFoundException;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.model.Person;
import edu.java.bot.repositories.LinkUpdateRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LinkUpdateServiceImpl implements LinkUpdateService {

    private final ChatService chatService;
    private final LinkUpdateRepository repository;

    public LinkUpdateServiceImpl(ChatService chatService, LinkUpdateRepository repository) {
        this.chatService = chatService;
        this.repository = repository;
    }

    @Override
    public void update(LinkUpdate update) {
        List<Person> personList = chatService.getByIds(update.getTgChatIds());
        checkIds(update.getTgChatIds(), personList);
        repository.save(update);
    }

    private void checkIds(List<Long> ids, List<Person> personList)  {
        Set<Long> idSet = personList.stream().map(Person::getId).collect(Collectors.toSet());
        ids.forEach(idSet::remove);
        if (!ids.isEmpty()) {
            throw new ChatNotFoundException(ids);
        }
    }
}
