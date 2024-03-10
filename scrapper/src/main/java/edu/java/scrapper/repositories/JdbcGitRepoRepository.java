package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.row_mappers.GitRepositoryRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGitRepoRepository implements GitRepoRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGitRepoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(GitRepository repo) {
        jdbcTemplate
            .update("insert into git_repository (link_id, urn, last_check_at, last_update_at, last_push_at) VALUES (?, ?, ?, ?, ?)",
                repo.getLinkId(), repo.getUrn(), repo.getLastCheckAt(), repo.getLastUpdateAt(), repo.getLastPushAt());
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
    public List<GitRepository> findByLastCheckAtLessThanLimit10(OffsetDateTime dateTime) {
        return jdbcTemplate.query(
            "select * from git_repository where last_check_at < ?",
            new GitRepositoryRowMapper(),
            dateTime
        );
    }

    @Override
    public void batchUpdate(List<GitRepository> repositories) {
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
