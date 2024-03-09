package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitLink;
import edu.java.scrapper.model.TgChat;
import java.util.List;
import java.util.Optional;

public interface GitLinkRepository {

    void save(GitLink link);

    Optional<GitLink> findById(long id);

    boolean existsById(long id);

    void deleteById(long id);
}
