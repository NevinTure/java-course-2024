package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Limit;

public interface GitRepoRepository {

    GitRepository save(GitRepository repo);

    void deleteById(long id);

    List<GitRepository> findAll();

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);

    void saveAll(List<GitRepository> repositories);
}
