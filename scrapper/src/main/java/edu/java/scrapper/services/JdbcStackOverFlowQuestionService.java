package edu.java.scrapper.services;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JdbcStackOverFlowQuestionService implements StackOverFlowQuestionService {

    private final StackOverFlowQuestionRepository sofRepository;
    private final StackOverFlowLinkUpdater linkUpdater;
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)/\\S+");

    public JdbcStackOverFlowQuestionService(StackOverFlowQuestionRepository sofRepository,
        StackOverFlowLinkUpdater linkUpdater
    ) {
        this.sofRepository = sofRepository;
        this.linkUpdater = linkUpdater;
    }

    @Override
    public void createAndAdd(Link link) {
        Matcher matcher = QUESTION_PATTERN.matcher(link.getUrl().toString());
        if (matcher.find()) {
            StackOverFlowQuestion question = new StackOverFlowQuestion(link.getId(), matcher.group(1));
            linkUpdater.initializeSofQuestion(question);
            sofRepository.save(question);
        }
    }
}
