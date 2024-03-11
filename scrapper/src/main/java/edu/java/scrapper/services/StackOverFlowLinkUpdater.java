package edu.java.scrapper.services;

import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.utils.UpdateType;
import java.util.List;
import java.util.Map;

public interface StackOverFlowLinkUpdater extends LinkUpdater {

    List<UpdateType> processUpdates(List<StackOverFlowQuestion> questions);

    Map<Long, UpdateType> updateSofQuestions(List<StackOverFlowQuestion> questions);

}
