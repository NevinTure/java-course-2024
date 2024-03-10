package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import edu.java.scrapper.row_mappers.StackOverFlowQuestionRowMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcStackOverFlowQuestionRepository implements StackOverFlowQuestionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStackOverFlowQuestionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(StackOverFlowQuestion quest) {
        jdbcTemplate.update("insert into stackoverflow_question (link_id, urn, last_check_at, last_update_at) VALUES (?, ?, ?, ?)",
            quest.getLinkId(),
            quest.getUrn(),
            quest.getLastCheckAt(),
            quest.getLastUpdateAt()
        );
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from stackoverflow_question where id = ?", id);
    }

    @Override
    public List<StackOverFlowQuestion> findAll() {
        return jdbcTemplate.query("select * from stackoverflow_question", new StackOverFlowQuestionRowMapper());
    }

    @Override
    public List<StackOverFlowQuestion> findByLastCheckAtLessThanLimit10(OffsetDateTime dateTime) {
        return null;
    }
}
