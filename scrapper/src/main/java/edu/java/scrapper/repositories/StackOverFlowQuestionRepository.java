package edu.java.scrapper.repositories;

import edu.java.scrapper.model.StackOverFlowQuestion;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Limit;

public interface StackOverFlowQuestionRepository {

    StackOverFlowQuestion save(StackOverFlowQuestion quest);

    void deleteById(long id);

    List<StackOverFlowQuestion> findAll();

    List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);

    void saveAll(List<StackOverFlowQuestion> questions);
}
