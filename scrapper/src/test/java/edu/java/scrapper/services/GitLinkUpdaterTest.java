package edu.java.scrapper.services;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import java.net.URI;
import java.time.OffsetDateTime;
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

@SpringBootTest("app.git-base-url=http://localhost:8080")
@WireMockTest(httpPort = 8080)
public class GitLinkUpdaterTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;
    @Autowired
    private GitRepositoryService service;
    @Autowired
    private GitLinkUpdater linkUpdater;
    @MockBean
    private LinkUpdateSenderService linkUpdateSenderService;

    @Test
    @Transactional
    @Rollback
    public void testProcessUpdatesWhenNoUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.parse("2024-03-13T13:42:16Z");
        OffsetDateTime testTime = OffsetDateTime.now();
        String urn = "/new/repo9";
        GitRepository repo = createAndSaveRepoByUrnAndLastCheckAt(urn, testTime);
        repo.setLastUpdateAt(dateTime1);
        stubFor(get(urn + "/events?per_page=1").willReturn(okJson("""
            [
                {
                    "created_at": "2024-03-13T13:42:16Z",
                    "type": "PushEvent"
                }
            ]
            """)));

        //when
        List<UpdateType> types = linkUpdater.processUpdates(List.of(repo));

        //then
        assertThat(types.get(0)).isEqualTo(UpdateType.NOTHING);
        assertThat(repo.getLastUpdateAt()).isEqualTo(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testProcessUpdatesWhenRegularUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.parse("2024-03-13T13:42:16Z");
        OffsetDateTime testTime = OffsetDateTime.now();
        String urn = "/new/repo10";
        GitRepository repo = createAndSaveRepoByUrnAndLastCheckAt(urn, testTime);
        repo.setLastUpdateAt(dateTime1);
        stubFor(get(urn + "/events?per_page=1").willReturn(okJson("""
            [
                {
                    "created_at": "2024-03-13T14:42:16Z",
                    "type": "SomeEvent"
                }
            ]
            """)));

        //when
        List<UpdateType> types = linkUpdater.processUpdates(List.of(repo));

        //then
        assertThat(types.get(0)).isEqualTo(UpdateType.UPDATE);
        assertThat(repo.getLastUpdateAt()).isAfter(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testProcessUpdateWhenPushEventUpdate() {
        //given
        OffsetDateTime dateTime1 = OffsetDateTime.parse("2024-03-13T13:42:16Z");
        OffsetDateTime testTime = OffsetDateTime.now();
        String urn = "/new/repo11";
        GitRepository repo = createAndSaveRepoByUrnAndLastCheckAt(urn, testTime);
        repo.setLastUpdateAt(dateTime1);
        repo.setLastPushAt(dateTime1);
        stubFor(get(urn + "/events?per_page=1").willReturn(okJson("""
            [
                {
                    "created_at": "2024-03-13T14:42:16Z",
                    "type": "PushEvent"
                }
            ]
            """)));

        //when
        List<UpdateType> types = linkUpdater.processUpdates(List.of(repo));

        //then
        assertThat(types.get(0)).isEqualTo(UpdateType.PUSH);
        assertThat(repo.getLastUpdateAt()).isAfter(dateTime1);
        assertThat(repo.getLastPushAt()).isAfter(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGitReposWhenNoUpdates() {
        //given
        String urn = "/new/repo12";
        OffsetDateTime dateTime1 = OffsetDateTime.parse("2024-03-13T13:42:16Z");
        OffsetDateTime testTime = OffsetDateTime.now().withNano(0);
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        GitRepository repo = new GitRepository(linkId, urn);
        repo.setLastUpdateAt(dateTime1);
        repo.setLastCheckAt(testTime);
        service.save(repo);
        stubFor(get(urn + "/events?per_page=1").willReturn(okJson("""
            [
                {
                    "created_at": "2024-03-13T13:42:16Z",
                    "type": "PushEvent"
                }
            ]
            """)));

        //when
        Map<Long, UpdateType> updateTypeMap = linkUpdater.updateGitRepos(List.of(repo));

        //then
        Optional<GitRepository> foundRepo = service.findAll()
            .stream().filter(v -> Objects.equals(v.getId(), repo.getId())).findFirst();
        assertThat(updateTypeMap).isEmpty();
        assertThat(foundRepo).isPresent();
        assertThat(foundRepo.get().getLastUpdateAt()).isEqualTo(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGitReposWhenUpdate() {
        //given
        String urn = "/new/repo13";
        OffsetDateTime dateTime1 = OffsetDateTime.parse("2024-03-13T13:42:16Z");
        OffsetDateTime testTime = OffsetDateTime.now().withNano(0);
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        GitRepository repo = new GitRepository(linkId, urn);
        repo.setLastUpdateAt(dateTime1);
        repo.setLastCheckAt(testTime);
        service.save(repo);
        stubFor(get(urn + "/events?per_page=1").willReturn(okJson("""
            [
                {
                    "created_at": "2024-03-13T15:42:16Z",
                    "type": "PushEvent"
                }
            ]
            """)));

        //when
        Map<Long, UpdateType> updateTypeMap = linkUpdater.updateGitRepos(List.of(repo));

        //then
        Optional<GitRepository> foundRepo = service.findAll()
            .stream().filter(v -> Objects.equals(v.getId(), repo.getId())).findFirst();
        assertThat(updateTypeMap).containsKey(repo.getLinkId());
        assertThat(updateTypeMap.get(repo.getLinkId())).isEqualTo(UpdateType.PUSH);
        assertThat(foundRepo).isPresent();
        assertThat(foundRepo.get().getLastUpdateAt()).isAfter(dateTime1);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdate() {
        //given
        String urn = "/new/repo14";
        OffsetDateTime dateTime1 = OffsetDateTime.parse("2024-03-13T13:42:16Z");
        OffsetDateTime testTime = OffsetDateTime.now().minusSeconds(20).withNano(0);
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        GitRepository repo = new GitRepository(linkId, urn);
        repo.setLastUpdateAt(dateTime1);
        repo.setLastCheckAt(testTime);
        service.save(repo);
        stubFor(get(urn + "/events?per_page=1").willReturn(okJson("""
            [
                {
                    "created_at": "2024-03-13T15:42:16Z",
                    "type": "PushEvent"
                }
            ]
            """)));


        //when
        linkUpdater.update();

        //then
        Mockito.verify(linkUpdateSenderService)
            .sendUpdate(eq(link), eq(UpdateType.PUSH));
    }


    private GitRepository createAndSaveRepoByUrnAndLastCheckAt(String urn, OffsetDateTime lastCheckAt) {
        Link link = new Link(
            buildUri(urn)
        );
        long linkId = linkService.save(link).getId();
        GitRepository repo = new GitRepository(linkId, urn);
        repo.setLastCheckAt(lastCheckAt.withNano(0));
        service.save(repo);
        return repo;
    }

    private URI buildUri(String urn) {
        return UriComponentsBuilder.newInstance().path("https://github.com").path(urn).build().toUri();
    }
}
