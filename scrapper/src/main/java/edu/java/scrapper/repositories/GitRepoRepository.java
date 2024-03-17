package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitRepoRepository {

    GitRepository save(GitRepository repository);

    void deleteById(long id);

    List<GitRepository> findAll();

    List<GitRepository> findByLastCheckAtLessThanLimit10(OffsetDateTime dateTime);

    void batchUpdate(List<GitRepository> repositories);
}
