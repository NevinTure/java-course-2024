package edu.java.scrapper.services;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.utils.UpdateType;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(properties = {"app.sof-base-url=http://localhost:8080"})
@WireMockTest(httpPort = 8080)
public class StackOverFlowLinkUpdaterTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;
    @Autowired
    private StackOverFlowQuestionService service;
    @Autowired
    private StackOverFlowLinkUpdater linkUpdater;
    @MockBean
    private LinkUpdateSenderService linkUpdateSenderService;

    @Test
    @Transactional
    @Rollback
    public void testProcessUpdatesWhenNoUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1674740385), ZoneOffset.UTC);
        OffsetDateTime testTime = OffsetDateTime.now();
        String urn = "9";
        StackOverFlowQuestion question = createAndSaveQuestionByUrnAndLastCheckAt(urn, testTime);
        question.setLastUpdateAt(dateTime1);
        stubFor(get("/" + urn + "?site=stackoverflow").willReturn(okJson("""
            {
                "items" : [{
                    "last_activity_date": 1674740385,
                    "answer_count": 0
                }]
            }
            """))
        );

        //when
        List<UpdateType> types = linkUpdater.processUpdates(List.of(question));

        //then
        assertThat(types.get(0)).isEqualTo(UpdateType.NOTHING);
        assertThat(question.getLastUpdateAt()).isEqualTo(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testProcessUpdatesWhenRegularUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1674740385), ZoneOffset.UTC);
        OffsetDateTime testTime = OffsetDateTime.now();
        String urn = "10";
        StackOverFlowQuestion question = createAndSaveQuestionByUrnAndLastCheckAt(urn, testTime);
        question.setLastUpdateAt(dateTime1);
        stubFor(get("/" + urn + "?site=stackoverflow").willReturn(okJson("""
            {
                "items" : [{
                    "last_activity_date": 1674741385,
                    "answer_count": 0
                }]
            }
            """))
        );

        //when
        List<UpdateType> types = linkUpdater.processUpdates(List.of(question));

        //then
        assertThat(types.get(0)).isEqualTo(UpdateType.UPDATE);
        assertThat(question.getLastUpdateAt()).isAfter(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testProcessUpdateWhenNewAnswerUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1674740385), ZoneOffset.UTC);
        OffsetDateTime testTime = OffsetDateTime.now();
        String urn = "11";
        StackOverFlowQuestion question = createAndSaveQuestionByUrnAndLastCheckAt(urn, testTime);
        question.setLastUpdateAt(dateTime1);
        stubFor(get("/" + urn + "?site=stackoverflow").willReturn(okJson("""
            {
                "items" : [{
                    "last_activity_date": 1674741385,
                    "answer_count": 1
                }]
            }
            """))
        );

        //when
        List<UpdateType> types = linkUpdater.processUpdates(List.of(question));

        //then
        assertThat(types.get(0)).isEqualTo(UpdateType.ANSWER);
        assertThat(question.getLastUpdateAt()).isAfter(dateTime1);
        assertThat(question.getAnswers()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateSofQuestionsWhenNoUpdates() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1674740385), ZoneOffset.UTC);
        OffsetDateTime testTime = OffsetDateTime.now().withNano(0);
        String urn = "12";
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        StackOverFlowQuestion question = new StackOverFlowQuestion(linkId, urn);
        question.setLastUpdateAt(dateTime1);
        question.setLastCheckAt(testTime);
        service.save(question);
        stubFor(get("/" + urn + "?site=stackoverflow").willReturn(okJson("""
            {
                "items" : [{
                    "last_activity_date": 1674740385,
                    "answer_count": 0
                }]
            }
            """))
        );

        //when
        Map<Long, UpdateType> updateTypeMap = linkUpdater.updateSofQuestions(List.of(question));

        //then
        Optional<StackOverFlowQuestion> foundRepo = service.findAll()
            .stream().filter(v -> Objects.equals(v.getId(), question.getId())).findFirst();
        assertThat(updateTypeMap).isEmpty();
        assertThat(foundRepo).isPresent();
        assertThat(foundRepo.get().getLastUpdateAt()).isEqualTo(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateSofQuestionsWhenUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1674740385), ZoneOffset.UTC);
        OffsetDateTime testTime = OffsetDateTime.now().withNano(0);
        String urn = "13";
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        StackOverFlowQuestion question = new StackOverFlowQuestion(linkId, urn);
        question.setLastUpdateAt(dateTime1);
        question.setLastCheckAt(testTime);
        service.save(question);
        stubFor(get("/" + urn + "?site=stackoverflow").willReturn(okJson("""
            {
                "items" : [{
                    "last_activity_date": 1674741385,
                    "answer_count": 1
                }]
            }
            """))
        );

        //when
        Map<Long, UpdateType> updateTypeMap = linkUpdater.updateSofQuestions(List.of(question));

        //then
        Optional<StackOverFlowQuestion> foundRepo = service.findAll()
            .stream().filter(v -> Objects.equals(v.getId(), question.getId())).findFirst();
        assertThat(updateTypeMap).containsKey(question.getLinkId());
        assertThat(updateTypeMap.get(question.getLinkId())).isEqualTo(UpdateType.ANSWER);
        assertThat(foundRepo).isPresent();
        assertThat(foundRepo.get().getLastUpdateAt()).isAfter(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1674740385), ZoneOffset.UTC);
        OffsetDateTime testTime = OffsetDateTime.now().minusSeconds(20).withNano(0);
        String urn = "14";
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        link.setId(linkId);
        StackOverFlowQuestion question = new StackOverFlowQuestion(linkId, urn);
        question.setLastUpdateAt(dateTime1);
        question.setLastCheckAt(testTime);
        service.save(question);
        stubFor(get("/" + urn + "?site=stackoverflow").willReturn(okJson("""
            {
                "items" : [{
                    "last_activity_date": 1674741385,
                    "answer_count": 1
                }]
            }
            """))
        );

        //when
        linkUpdater.update();

        //then
        Mockito.verify(linkUpdateSenderService)
            .sendUpdate(eq(link), eq(UpdateType.ANSWER));
    }

    private StackOverFlowQuestion createAndSaveQuestionByUrnAndLastCheckAt(String urn, OffsetDateTime lastCheckAt) {
        URI url = buildUri(urn);
        Link link = new Link(
            url);
        long linkId = linkService.save(link).getId();
        StackOverFlowQuestion question = new StackOverFlowQuestion(linkId, urn);
        question.setLastCheckAt(lastCheckAt.withNano(0));
        service.save(question);
        return question;
    }

    private URI buildUri(String urn) {
        return UriComponentsBuilder.newInstance().path("https://stackoverflow.com/questions/").path(urn).build()
            .toUri();
    }
}
