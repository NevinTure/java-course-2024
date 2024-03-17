package edu.java.scrapper.services;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.GitRepoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class GitRepositoryServiceTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;
    @Autowired
    private GitRepositoryService service;
    @Autowired
    private GitRepoRepository gitRepository;
    @MockBean
    private GitLinkUpdater linkUpdater;

    @Test
    @Transactional
    @Rollback
    public void testCreateAndSave() {
        //given
        Link link = new Link(URI.create("https://github.com/new/repo2"));
        long linkId = linkService.save(link).getId();

        //when
        GitRepository repository = service.createAndSave(link);

        //then
        assertThat(gitRepository.findAll()).contains(repository);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByLastCheckAtLessThan() {
        //given
        String urn1 = "/new/repo3";
        String urn2 = "/new/repo4";
        String urn3 = "/new/repo5";
        GitRepository gitRepository1 =
            createAndSaveRepoByUrnAndLastCheckAt(urn1, OffsetDateTime.now().minusSeconds(15));
        GitRepository gitRepository2 =
            createAndSaveRepoByUrnAndLastCheckAt(urn2, OffsetDateTime.now());
        GitRepository gitRepository3 =
            createAndSaveRepoByUrnAndLastCheckAt(urn3, OffsetDateTime.now().minusSeconds(15));

        //when
        List<GitRepository> result = service.findByLastCheckAtLessThan(OffsetDateTime.now().minusSeconds(10));

        //then
        assertThat(result).contains(gitRepository1, gitRepository3);
    }

    @Test
    @Transactional
    @Rollback
    public void testBatchUpdate() {
        //given
        String urn1 = "/new/repo6";
        String urn2 = "/new/repo7";
        String urn3 = "/new/repo8";
        GitRepository gitRepository1 =
            createAndSaveRepoByUrnAndLastCheckAt(urn1, OffsetDateTime.now().minusSeconds(15));
        GitRepository gitRepository2 =
            createAndSaveRepoByUrnAndLastCheckAt(urn2, OffsetDateTime.now().minusSeconds(15));
        GitRepository gitRepository3 =
            createAndSaveRepoByUrnAndLastCheckAt(urn3, OffsetDateTime.now().minusSeconds(15));

        //when
        OffsetDateTime updatedValue = OffsetDateTime.now().withNano(0);
        gitRepository1.setLastCheckAt(updatedValue);
        gitRepository2.setLastCheckAt(updatedValue);
        gitRepository3.setLastCheckAt(updatedValue);
        List<GitRepository> repos = List.of(gitRepository1, gitRepository2, gitRepository3);
        service.batchUpdate(repos);

        //then
        assertThat(gitRepository.findAll()).contains(gitRepository1, gitRepository2, gitRepository3);
    }

    private GitRepository createAndSaveRepoByUrnAndLastCheckAt(String urn, OffsetDateTime lastCheckAt) {
        Link link = new Link(
            UriComponentsBuilder.newInstance().path("https://github.com").path(urn).build().toUri()
        );
        long linkId = linkService.save(link).getId();
        GitRepository repo = new GitRepository(linkId, urn);
        repo.setLastCheckAt(lastCheckAt.withNano(0));
        service.save(repo);
        return repo;
    }
}
