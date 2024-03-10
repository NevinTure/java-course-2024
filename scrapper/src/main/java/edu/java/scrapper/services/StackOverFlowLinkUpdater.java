package edu.java.scrapper.services;

import edu.java.scrapper.model.StackOverFlowQuestion;
import java.util.List;

public interface StackOverFlowLinkUpdater extends LinkUpdater {

    void initializeSofQuestion(StackOverFlowQuestion question);

    int updateSofQuestions(List<StackOverFlowQuestion> questions);

}
