package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitLink;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.row_mappers.GitLinkRowMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JdbcGitLinkRepository implements GitLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGitLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(GitLink link) {
        if (existsById(link.getId())) {
            jdbcTemplate.update("update git_link set last_check_at = ?, last_push_at = ?, last_update_at = ? where id = ?",
                link.getLastCheckAt(), link.getLastPushAt(), link.getLastUpdateAt(), link.getId());
        } else {
            jdbcTemplate
                .update("insert into git_link (url, last_check_at, last_update_at, last_push_at) values (?, ?, ?, ?)",
                    link.getUrl(), link.getLastCheckAt(), link.getLastUpdateAt(), link.getLastPushAt());
        }
    }

    @Override
    public Optional<GitLink> findById(long id) {
        List<GitLink> links = jdbcTemplate
            .query("select * from git_link where id = ?", new GitLinkRowMapper(), id);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public boolean existsById(long id) {
        List<GitLink> links = jdbcTemplate
            .query("select * from git_link where id = ?", new GitLinkRowMapper(), id);
        return !links.isEmpty();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from git_link where id = ?", id);
    }
}
