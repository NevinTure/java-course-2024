package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.row_mappers.GitRepositoryRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

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
}
