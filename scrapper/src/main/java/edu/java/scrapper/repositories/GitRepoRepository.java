package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.row_mappers.GitRepositoryRowMapper;
import org.springframework.data.domain.Limit;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitRepoRepository {

    public GitRepository save(GitRepository repo);

    public void deleteById(long id);

    public List<GitRepository> findAll();

    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);

    public void saveAll(List<GitRepository> repositories);
}
