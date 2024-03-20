package edu.java.scrapper.repositories;

import edu.java.scrapper.model.StackOverFlowQuestion;
import java.time.OffsetDateTime;
import java.util.List;

public interface StackOverFlowQuestionRepository {

    StackOverFlowQuestion save(StackOverFlowQuestion question);

    void deleteById(long id);

    List<StackOverFlowQuestion> findAll();

    List<StackOverFlowQuestion> findByLastCheckAtLessThanLimit10(OffsetDateTime dateTime);

    void saveAll(List<StackOverFlowQuestion> questions);
}
