package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import edu.java.scrapper.services.StackOverFlowLinkUpdater;
import edu.java.scrapper.services.StackOverFlowQuestionService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcStackOverFlowQuestionService implements StackOverFlowQuestionService {

    private final StackOverFlowQuestionRepository sofRepository;
    private final StackOverFlowLinkUpdater linkUpdater;
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)(/\\S+)?");

    public JdbcStackOverFlowQuestionService(
        StackOverFlowQuestionRepository sofRepository,
        @Lazy StackOverFlowLinkUpdater linkUpdater
    ) {
        this.sofRepository = sofRepository;
        this.linkUpdater = linkUpdater;
    }

    @Override
    public void save(StackOverFlowQuestion question) {
        sofRepository.save(question);
    }

    @Override
    public StackOverFlowQuestion createAndSave(Link link) {
        Matcher matcher = QUESTION_PATTERN.matcher(link.getUrl().toString());
        if (matcher.find()) {
            StackOverFlowQuestion question = new StackOverFlowQuestion(link.getId(), matcher.group(1));
            linkUpdater.processUpdates(List.of(question));
            save(question);
            return question;
        }
        return null;
    }

    @Override
    public List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime) {
        return sofRepository.findByLastCheckAtLessThan(dateTime);
    }

    @Override
    public List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime, int limit) {
        return sofRepository.findByLastCheckAtLessThan(dateTime, Limit.of(limit));
    }

    @Override
    public void batchUpdate(List<StackOverFlowQuestion> questions) {
        sofRepository.saveAll(questions);
    }

    @Override
    public List<StackOverFlowQuestion> findAll() {
        return sofRepository.findAll();
    }
}
