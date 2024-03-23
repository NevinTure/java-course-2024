package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.GitRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaGitRepoRepository extends JpaRepository<GitRepository, Long> {

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);
}
