package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitLink;
import edu.java.scrapper.model.StackOverFlowLink;
import edu.java.scrapper.row_mappers.GitLinkRowMapper;
import edu.java.scrapper.row_mappers.StackOverFlowLinkRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStackOverFlowRepository implements StackOverFlowLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStackOverFlowRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(StackOverFlowLink link) {
        if (existsById(link.getId())) {
            jdbcTemplate.update("update stackoverflow_link set last_check_at = ?, answers = ?, last_update_at = ? where id = ?",
                link.getLastCheckAt(), link.getAnswers(), link.getLastUpdateAt(), link.getId());
        } else {
            jdbcTemplate
                .update("insert into stackoverflow_link (url, last_check_at, last_update_at, answers) values (?, ?, ?, ?)",
                    link.getUrl(), link.getLastCheckAt(), link.getLastUpdateAt(), link.getAnswers());
        }
    }

    @Override
    public Optional<StackOverFlowLink> findById(long id) {
        List<StackOverFlowLink> links = jdbcTemplate
            .query("select * from stackoverflow_link where id = ?", new StackOverFlowLinkRowMapper(), id);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public boolean existsById(long id) {
        List<StackOverFlowLink> links = jdbcTemplate
            .query("select * from stackoverflow_link where id = ?", new StackOverFlowLinkRowMapper(), id);
        return !links.isEmpty();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from stackoverflow_link where id = ?", id);
    }
}
