package edu.java.scrapper.services;

import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.dtos.Item;
import edu.java.scrapper.dtos.StackOverflowResponse;
import edu.java.scrapper.model.StackOverFlowQuestion;
import java.util.List;

public class StackOverFlowLinkUpdaterImpl implements StackOverFlowLinkUpdater {

    private final StackOverFlowQuestionService questionService;
    private final LinkService linkService;
    private final StackOverflowClient sofClient;

    public StackOverFlowLinkUpdaterImpl(
        StackOverFlowQuestionService questionService,
        LinkService linkService,
        StackOverflowClient sofClient
    ) {
        this.questionService = questionService;
        this.linkService = linkService;
        this.sofClient = sofClient;
    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public void initializeSofQuestion(StackOverFlowQuestion question) {
        StackOverflowResponse response = sofClient.getUpdateInfo(List.of(question.getUrn()));
        Item item = response.getItems().get(0);
        question.setLastUpdateAt(item.getDateTime());
        question.setLastCheckAt(item.getDateTime());
        question.setAnswers(item.getAnswerCount());
    }

    @Override
    public int updateSofQuestions(List<StackOverFlowQuestion> questions) {
        return 0;
    }
}
