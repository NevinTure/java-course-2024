package edu.java.scrapper.repositories;

import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.row_mappers.StackOverFlowQuestionRowMapper;
import org.springframework.data.domain.Limit;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public interface StackOverFlowQuestionRepository {

    public StackOverFlowQuestion save(StackOverFlowQuestion quest);

    public void deleteById(long id);

    public List<StackOverFlowQuestion> findAll();

    public List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    public List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);

    public void saveAll(List<StackOverFlowQuestion> questions);
}
