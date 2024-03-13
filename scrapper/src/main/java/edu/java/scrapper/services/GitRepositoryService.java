package edu.java.scrapper.services;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitRepositoryService {

    void save(GitRepository repository);

    GitRepository createAndSave(Link link);

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime offsetDateTime);

    void batchUpdate(List<GitRepository> repositories);
}
