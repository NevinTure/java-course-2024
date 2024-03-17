package edu.java.scrapper.services;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class StackOverFlowQuestionServiceTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;
    @Autowired
    private StackOverFlowQuestionService service;
    @Autowired
    private StackOverFlowQuestionRepository sofRepository;
    @MockBean
    private StackOverFlowLinkUpdater linkUpdater;

    @Test
    @Transactional
    @Rollback
    public void testCreateAndSave() {
        //given
        Link link = new Link(URI.create("https://stackoverflow.com/questions/1"));
        long linkId = linkService.save(link).getId();
        link.setId(linkId);

        //when
        StackOverFlowQuestion question = service.createAndSave(link);

        //then
        assertThat(sofRepository.findAll()).contains(question);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByLastCheckAtLessThan() {
        //given
        String urn1 = "2";
        String urn2 = "3";
        String urn3 = "4";
        StackOverFlowQuestion question1 =
            createAndSaveQuestionByUrnAndLastCheckAt(urn1, OffsetDateTime.now());
        StackOverFlowQuestion question2 =
            createAndSaveQuestionByUrnAndLastCheckAt(urn2, OffsetDateTime.now().minusSeconds(15));
        StackOverFlowQuestion question3 =
            createAndSaveQuestionByUrnAndLastCheckAt(urn3, OffsetDateTime.now().minusSeconds(15));
        //when
        List<StackOverFlowQuestion> result = service.findByLastCheckAtLessThan(OffsetDateTime.now().minusSeconds(10));

        //then
        assertThat(result).contains(question2, question3);
    }

    @Test
    @Transactional
    @Rollback
    public void testBatchUpdate() {
        //given
        String urn1 = "5";
        String urn2 = "6";
        String urn3 = "7";
        StackOverFlowQuestion question1 =
            createAndSaveQuestionByUrnAndLastCheckAt(urn1, OffsetDateTime.now().minusSeconds(15));
        StackOverFlowQuestion question2 =
            createAndSaveQuestionByUrnAndLastCheckAt(urn2, OffsetDateTime.now().minusSeconds(15));
        StackOverFlowQuestion question3 =
            createAndSaveQuestionByUrnAndLastCheckAt(urn3, OffsetDateTime.now().minusSeconds(15));

        //when
        OffsetDateTime updatedValue = OffsetDateTime.now().withNano(0);
        question1.setLastCheckAt(updatedValue);
        question2.setLastCheckAt(updatedValue);
        question3.setLastCheckAt(updatedValue);
        List<StackOverFlowQuestion> repos = List.of(question1, question2, question3);
        service.batchUpdate(repos);

        //then
        assertThat(sofRepository.findAll()).contains(question1, question2, question3);
    }

    private StackOverFlowQuestion createAndSaveQuestionByUrnAndLastCheckAt(String urn, OffsetDateTime lastCheckAt) {
        Link link = new Link(
            UriComponentsBuilder.newInstance().path("https://stackoverflow.com/questions").path(urn).build().toUri());
        long linkId = linkService.save(link).getId();
        StackOverFlowQuestion question = new StackOverFlowQuestion(linkId, urn);
        question.setLastCheckAt(lastCheckAt.withNano(0));
        service.save(question);
        return question;
    }
}
