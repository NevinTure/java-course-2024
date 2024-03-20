package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitRepoRepository {

    void save(GitRepository repository);

    void deleteById(long id);

    List<GitRepository> findAll();

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    void batchUpdate(List<GitRepository> repositories);
}
