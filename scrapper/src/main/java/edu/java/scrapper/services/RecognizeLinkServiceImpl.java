package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import org.springframework.stereotype.Service;

@Service
public class RecognizeLinkServiceImpl implements RecognizeLinkService {

    private final GitRepositoryService gitRepositoryService;
    private final StackOverFlowQuestionService stackOverFlowQuestionService;

    public RecognizeLinkServiceImpl(
        GitRepositoryService gitRepositoryService,
        StackOverFlowQuestionService stackOverFlowQuestionService
    ) {
        this.gitRepositoryService = gitRepositoryService;
        this.stackOverFlowQuestionService = stackOverFlowQuestionService;
    }

    @Override
    public void recognize(Link link) {
        String url = link.getUrl().toString();
        if (url.contains("github.com")) {
            gitRepositoryService.createAndSave(link);
        } else if (url.contains("stackoverflow.com")) {
            stackOverFlowQuestionService.createAndAdd(link);
        }
    }
}
