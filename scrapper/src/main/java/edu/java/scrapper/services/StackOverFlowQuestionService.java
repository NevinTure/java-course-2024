package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import java.time.OffsetDateTime;
import java.util.List;

public interface StackOverFlowQuestionService {

    void createAndAdd(Link link);

    List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    void batchUpdate(List<StackOverFlowQuestion> questions);
}
