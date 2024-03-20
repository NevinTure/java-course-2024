package edu.java.scrapper.repositories.jdbc;

import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import edu.java.scrapper.row_mappers.StackOverFlowQuestionRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcStackOverFlowQuestionRepository implements StackOverFlowQuestionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStackOverFlowQuestionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StackOverFlowQuestion save(StackOverFlowQuestion quest) {
        Long id = jdbcTemplate
            .queryForObject("insert into stackoverflow_question (link_id, urn, last_check_at, last_update_at)"
                + " VALUES (?, ?, ?, ?) returning id", Long.class,
            quest.getLinkId(),
            quest.getUrn(),
            quest.getLastCheckAt(),
            quest.getLastUpdateAt()
        );
        quest.setId(id);
        return quest;
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
        return jdbcTemplate.query(
            "select * from stackoverflow_question where last_check_at < ? limit 10",
            new StackOverFlowQuestionRowMapper(),
            dateTime
        );
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public void saveAll(List<StackOverFlowQuestion> questions) {
        jdbcTemplate.batchUpdate(
            "update stackoverflow_question set last_check_at = ?, last_update_at = ?, answers = ? where id = ?",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    StackOverFlowQuestion question = questions.get(i);
                    ps.setObject(1, question.getLastCheckAt());
                    ps.setObject(2, question.getLastUpdateAt());
                    ps.setObject(3, question.getAnswers());
                    ps.setLong(4, question.getId());
                }

                @Override
                public int getBatchSize() {
                    return questions.size();
                }
            }
        );
    }
}
