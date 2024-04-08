package edu.java.scrapper.repositories.jooq;

import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Records;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.model.jooq.Tables.STACKOVERFLOW_QUESTION;

public class JooqStackOverFlowQuestionRepository implements StackOverFlowQuestionRepository {

    private final DSLContext context;

    public JooqStackOverFlowQuestionRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public StackOverFlowQuestion save(StackOverFlowQuestion quest) {
        Record1<Long> idRecord = context
            .insertInto(STACKOVERFLOW_QUESTION,
                STACKOVERFLOW_QUESTION.LINK_ID, STACKOVERFLOW_QUESTION.URN, STACKOVERFLOW_QUESTION.LAST_CHECK_AT,
                STACKOVERFLOW_QUESTION.LAST_UPDATE_AT, STACKOVERFLOW_QUESTION.ANSWERS
            ).values(quest.getLinkId(), quest.getUrn(),
                quest.getLastCheckAt(), quest.getLastUpdateAt(), quest.getAnswers()
            ).returningResult(STACKOVERFLOW_QUESTION.ID)
            .fetchOne();
        quest.setId(idRecord.component1());
        return quest;
    }

    @Override
    public void deleteById(long id) {
        context
            .delete(STACKOVERFLOW_QUESTION).where(STACKOVERFLOW_QUESTION.ID.eq(id)).execute();
    }

    @Override
    public List<StackOverFlowQuestion> findAll() {
        return context
            .select(
                STACKOVERFLOW_QUESTION.ID,
                STACKOVERFLOW_QUESTION.LINK_ID,
                STACKOVERFLOW_QUESTION.URN,
                STACKOVERFLOW_QUESTION.LAST_CHECK_AT,
                STACKOVERFLOW_QUESTION.LAST_UPDATE_AT,
                STACKOVERFLOW_QUESTION.ANSWERS
            ).from(STACKOVERFLOW_QUESTION).fetch().map((Records.mapping(this::mapper)));
    }

    @Override
    public List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime) {
        return context
            .select(STACKOVERFLOW_QUESTION.ID,
                STACKOVERFLOW_QUESTION.LINK_ID,
                STACKOVERFLOW_QUESTION.URN,
                STACKOVERFLOW_QUESTION.LAST_CHECK_AT,
                STACKOVERFLOW_QUESTION.LAST_UPDATE_AT,
                STACKOVERFLOW_QUESTION.ANSWERS)
            .from(STACKOVERFLOW_QUESTION).where(STACKOVERFLOW_QUESTION.LAST_CHECK_AT.le(dateTime))
            .fetch().map((Records.mapping(this::mapper)));
    }

    @Override
    public List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit) {
        return context
            .select(STACKOVERFLOW_QUESTION.ID,
                STACKOVERFLOW_QUESTION.LINK_ID,
                STACKOVERFLOW_QUESTION.URN,
                STACKOVERFLOW_QUESTION.LAST_CHECK_AT,
                STACKOVERFLOW_QUESTION.LAST_UPDATE_AT,
                STACKOVERFLOW_QUESTION.ANSWERS)
            .from(STACKOVERFLOW_QUESTION).where(STACKOVERFLOW_QUESTION.LAST_CHECK_AT.le(dateTime))
            .limit(limit.max())
            .fetch().map((Records.mapping(this::mapper)));
    }

    @Override
    @SuppressWarnings("MagicNumber")
    @Transactional
    public void saveAll(List<StackOverFlowQuestion> questions) {
        context.batched(c -> {
            for (StackOverFlowQuestion quest : questions) {
                c.dsl().update(STACKOVERFLOW_QUESTION)
                    .set(STACKOVERFLOW_QUESTION.LAST_CHECK_AT, quest.getLastCheckAt())
                    .set(STACKOVERFLOW_QUESTION.LAST_UPDATE_AT, quest.getLastUpdateAt())
                    .set(STACKOVERFLOW_QUESTION.ANSWERS, quest.getAnswers())
                    .where(STACKOVERFLOW_QUESTION.ID.eq(quest.getId())).execute();
            }
        });
    }

    private StackOverFlowQuestion mapper(
        Long id, Long linkId, String urn, OffsetDateTime lastCheckAt, OffsetDateTime lastUpdateAt, Integer answers
    ) {
        StackOverFlowQuestion question = new StackOverFlowQuestion();
        question.setId(id);
        question.setLinkId(linkId);
        question.setUrn(urn);
        question.setLastCheckAt(lastCheckAt);
        question.setLastUpdateAt(lastUpdateAt);
        question.setAnswers(answers);
        return question;
    }
}
