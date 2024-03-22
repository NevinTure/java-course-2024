package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import org.springframework.data.domain.Limit;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitRepoRepository {

    GitRepository save(GitRepository repository);

    void deleteById(long id);

    List<GitRepository> findAll();

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);

    void saveAll(List<GitRepository> repositories);
}
