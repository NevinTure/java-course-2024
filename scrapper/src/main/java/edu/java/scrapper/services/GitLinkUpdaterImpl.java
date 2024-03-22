package edu.java.scrapper.services;

import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.dtos.GitHubResponse;
import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GitLinkUpdaterImpl implements GitLinkUpdater {

    private final LinkService linkService;
    private final GitRepositoryService repositoryService;
    private final GitHubClient gitHubClient;
    private final LinkUpdateSenderService linkUpdateSenderService;
    private static final int SECONDS_BETWEEN_UPDATES = 15;

    private static final int FIND_LIMIT = 10;

    public GitLinkUpdaterImpl(
        LinkService linkService, @Lazy GitRepositoryService repositoryService,
        GitHubClient gitHubClient,
        LinkUpdateSenderService linkUpdateSenderService
    ) {
        this.linkService = linkService;
        this.repositoryService = repositoryService;
        this.gitHubClient = gitHubClient;
        this.linkUpdateSenderService = linkUpdateSenderService;
    }

    @Override
    @Transactional
    public int update() {
        List<GitRepository> repositories =
            repositoryService.findByLastCheckAtLessThan(OffsetDateTime.now()
            .minusSeconds(SECONDS_BETWEEN_UPDATES).withNano(0), FIND_LIMIT);
        Map<Long, UpdateType> updatedLinkIds = updateGitRepos(repositories);
        Map<Link, UpdateType> updatedLinks = linkService.mapIdsToLinksWithUpdateType(updatedLinkIds);
        for (var entry : updatedLinks.entrySet()) {
            linkUpdateSenderService.sendUpdate(entry.getKey(), entry.getValue());
        }
        return updatedLinks.size();
    }

    @Override
    public Map<Long, UpdateType> updateGitRepos(List<GitRepository> repositories) {
        Map<Long, UpdateType> updatedLinks = new HashMap<>(repositories.size());
        List<UpdateType> updateTypes = processUpdates(repositories);
        for (int i = 0; i < updateTypes.size(); i++) {
            if (!updateTypes.get(i).equals(UpdateType.NOTHING)) {
                updatedLinks.put(repositories.get(i).getLinkId(), updateTypes.get(i));
            }
        }
        repositoryService.batchUpdate(repositories);
        return updatedLinks;
    }

    @Override
    public List<UpdateType> processUpdates(List<GitRepository> repositories) {
        List<UpdateType> updateTypes = new ArrayList<>(repositories.size());
        for (GitRepository repo : repositories) {
            List<GitHubResponse> responses = gitHubClient.getUpdateInfo(repo.getUrn());
            if (!responses.isEmpty()) {
                GitHubResponse response = responses.get(0);
                updateTypes.add(checkAndUpdate(repo, response));
            } else {
                updateTypes.add(UpdateType.NOTHING);
            }
        }
        return updateTypes;
    }

    private UpdateType checkAndUpdate(GitRepository repository, GitHubResponse response) {
        repository.setLastCheckAt(OffsetDateTime.now().withNano(0));
        if (response.getDateTime().isAfter(repository.getLastUpdateAt())) {
            return checkSpecificUpdate(repository, response);
        }
        return UpdateType.NOTHING;
    }

    private UpdateType checkSpecificUpdate(GitRepository repository, GitHubResponse response) {
        if (response.getType().equals("PushEvent")) {
            repository.setLastUpdateAt(response.getDateTime());
            repository.setLastPushAt(response.getDateTime());
            return UpdateType.PUSH;
        } else {
            repository.setLastUpdateAt(response.getDateTime());
            return UpdateType.UPDATE;
        }
    }
}
