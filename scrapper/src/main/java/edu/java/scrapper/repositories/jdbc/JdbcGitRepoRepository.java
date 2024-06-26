package edu.java.scrapper.repositories.jdbc;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.repositories.GitRepoRepository;
import edu.java.scrapper.row_mappers.GitRepositoryRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class JdbcGitRepoRepository implements GitRepoRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGitRepoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GitRepository save(GitRepository repo) {
        Long id = jdbcTemplate
            .queryForObject("insert into git_repository (link_id, urn, last_check_at, last_update_at, last_push_at)"
                    + " VALUES (?, ?, ?, ?, ?) returning id", Long.class,
                repo.getLinkId(), repo.getUrn(), repo.getLastCheckAt(), repo.getLastUpdateAt(), repo.getLastPushAt());
        repo.setId(id);
        return repo;
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from git_repository where id = ?", id);
    }

    @Override
    public List<GitRepository> findAll() {
        return jdbcTemplate.query("select * from git_repository", new GitRepositoryRowMapper());
    }

    @Override
    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime) {
        return jdbcTemplate.query(
            "select * from git_repository where last_check_at < ?",
            new GitRepositoryRowMapper(),
            dateTime
        );
    }

    @Override
    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit) {
        return jdbcTemplate.query(
            "select * from git_repository where last_check_at < ? limit ?",
            new GitRepositoryRowMapper(),
            dateTime, limit.max()
        );
    }

    @Override
    @SuppressWarnings("MagicNumber")
    @Transactional
    public void saveAll(List<GitRepository> repositories) {
        jdbcTemplate.batchUpdate(
            "update git_repository set last_check_at = ?, last_update_at = ?, last_push_at = ? where id = ?",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    GitRepository repository = repositories.get(i);
                    ps.setObject(1, repository.getLastCheckAt());
                    ps.setObject(2, repository.getLastUpdateAt());
                    ps.setObject(3, repository.getLastPushAt());
                    ps.setLong(4, repository.getId());
                }

                @Override
                public int getBatchSize() {
                    return repositories.size();
                }
            }
        );
    }
}
