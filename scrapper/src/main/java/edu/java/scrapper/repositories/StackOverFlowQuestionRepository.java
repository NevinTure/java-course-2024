package edu.java.scrapper.repositories;

import edu.java.scrapper.model.StackOverFlowQuestion;
import java.util.List;

public interface StackOverFlowQuestionRepository {

    void save(StackOverFlowQuestion question);

    void deleteById(long id);

    List<StackOverFlowQuestion> findAll();
}
