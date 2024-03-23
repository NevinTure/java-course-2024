package edu.java.scrapper.services;

import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.dtos.Item;
import edu.java.scrapper.dtos.StackOverflowResponse;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.utils.UpdateType;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StackOverFlowLinkUpdaterImpl implements StackOverFlowLinkUpdater {

    private final StackOverFlowQuestionService questionService;
    private final LinkService linkService;
    private final StackOverflowClient sofClient;
    private final LinkUpdateSenderService linkUpdateSenderService;
    private static final int SECONDS_BETWEEN_UPDATES = 15;
    private static final int FIND_LIMIT = 10;

    public StackOverFlowLinkUpdaterImpl(
        StackOverFlowQuestionService questionService,
        LinkService linkService,
        StackOverflowClient sofClient,
        LinkUpdateSenderService linkUpdateSenderService
    ) {
        this.questionService = questionService;
        this.linkService = linkService;
        this.sofClient = sofClient;
        this.linkUpdateSenderService = linkUpdateSenderService;
    }

    @Override
    @Transactional
    public int update() {
        List<StackOverFlowQuestion> questions =
            questionService.findByLastCheckAtLessThan(OffsetDateTime.now()
            .withNano(0).minusSeconds(SECONDS_BETWEEN_UPDATES), FIND_LIMIT);
        if (questions.isEmpty()) {
            return 0;
        }
        Map<Long, UpdateType> updatedLinkIds = updateSofQuestions(questions);
        Map<Link, UpdateType> updatedLinks = linkService.mapIdsToLinksWithUpdateType(updatedLinkIds);
        for (var entry : updatedLinks.entrySet()) {
            linkUpdateSenderService.sendUpdate(entry.getKey(), entry.getValue());
        }
        return updatedLinks.size();
    }

    @Override
    public List<UpdateType> processUpdates(List<StackOverFlowQuestion> questions) {
        List<UpdateType> updateTypes = new ArrayList<>(questions.size());
        List<String> urns = questions.stream().map(StackOverFlowQuestion::getUrn).toList();
        StackOverflowResponse response = sofClient.getUpdateInfo(urns);
        List<Item> items = response.getItems();
        System.out.println(items);
        for (int i = 0; i < questions.size(); i++) {
            updateTypes.add(checkAndUpdate(questions.get(i), items.get(i)));
        }
        return updateTypes;
    }

    @Override
    public Map<Long, UpdateType> updateSofQuestions(List<StackOverFlowQuestion> questions) {
        Map<Long, UpdateType> updatedLinks = new HashMap<>(questions.size());
        List<UpdateType> updateTypes = processUpdates(questions);
        for (int i = 0; i < updateTypes.size(); i++) {
            if (!updateTypes.get(i).equals(UpdateType.NOTHING)) {
                updatedLinks.put(questions.get(i).getLinkId(), updateTypes.get(i));
            }
        }
        questionService.batchUpdate(questions);
        return updatedLinks;
    }

    private UpdateType checkAndUpdate(StackOverFlowQuestion question, Item item) {
        question.setLastCheckAt(OffsetDateTime.now().withNano(0));
        if (item.getDateTime().isAfter(question.getLastUpdateAt())) {
            question.setLastUpdateAt(item.getDateTime());
            System.out.println(true);
            return checkSpecificUpdate(question, item);
        } else {
            return UpdateType.NOTHING;
        }
    }

    private UpdateType checkSpecificUpdate(StackOverFlowQuestion question, Item item) {
        UpdateType type = UpdateType.UPDATE;
        System.out.println(item.getAnswerCount());
        if (question.getAnswers() < item.getAnswerCount()) {
            question.setAnswers(item.getAnswerCount());
            question.setLastUpdateAt(item.getDateTime());
            type = UpdateType.ANSWER;
        }
        return type;
    }
}
