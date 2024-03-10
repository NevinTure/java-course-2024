package edu.java.scrapper.services;

import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.dtos.GitHubResponse;
import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitLinkUpdaterImpl implements GitLinkUpdater {

    private final LinkService linkService;
    private final GitRepositoryService repositoryService;
    private final GitHubClient gitHubClient;
    private final LinkUpdateSenderService linkUpdateSenderService;
    private static final int SECONDS_BETWEEN_UPDATES = 15;

    public GitLinkUpdaterImpl(LinkService linkService, GitRepositoryService repositoryService,
        GitHubClient gitHubClient,
        LinkUpdateSenderService linkUpdateSenderService
    ) {
        this.linkService = linkService;
        this.repositoryService = repositoryService;
        this.gitHubClient = gitHubClient;
        this.linkUpdateSenderService = linkUpdateSenderService;
    }

    @Override
    public Map<Long, UpdateType> updateGitRepos(List<GitRepository> repositories) {
        Map<Long, UpdateType> updatedLinks = new HashMap<>(repositories.size());
        for (GitRepository nowRepo : repositories) {
            UpdateType type = processUpdate(nowRepo);
            if (!type.equals(UpdateType.NOTHING)) {
                updatedLinks.put(nowRepo.getLinkId(), type);
            }
        }
        repositoryService.batchUpdate(repositories);
        return updatedLinks;
    }

    @Override
    public int update() {
        List<GitRepository> repositories =
            repositoryService.findByLastCheckAtLessThan(OffsetDateTime.now()
            .minusSeconds(SECONDS_BETWEEN_UPDATES));
        Map<Long, UpdateType> updatedLinkIds = updateGitRepos(repositories);
        Map<Link, UpdateType> updatedLinks = mapIdsToLinks(updatedLinkIds);
        for (var entry : updatedLinks.entrySet()) {
            linkUpdateSenderService.sendUpdate(entry.getKey(), entry.getValue());
        }
        return updatedLinks.size();
    }

    @Override
    public UpdateType processUpdate(GitRepository repository) {
        List<GitHubResponse> responses = gitHubClient.getUpdateInfo(repository.getUrn());
        if (!responses.isEmpty()) {
            GitHubResponse response = responses.get(0);
            return checkAndUpdate(repository, response);
        }
        return UpdateType.NOTHING;
    }

    public UpdateType checkAndUpdate(GitRepository repository, GitHubResponse response) {
        repository.setLastCheckAt(OffsetDateTime.now());
        if (response.getDateTime().isAfter(repository.getLastUpdateAt())) {
            if (response.getType().equals("PushEvent")) {
                repository.setLastUpdateAt(response.getDateTime());
                repository.setLastPushAt(response.getDateTime());
                return UpdateType.PUSH;
            } else {
                repository.setLastUpdateAt(response.getDateTime());
                return UpdateType.UPDATE;
            }
        }
        return UpdateType.NOTHING;
    }

    private Map<Link, UpdateType> mapIdsToLinks(Map<Long, UpdateType> updatedLinkIds) {
        Map<Link, UpdateType> updatedLinksMap = new HashMap<>(updatedLinkIds.size());
        List<Link> foundLinks = linkService.findLinkByIds(updatedLinkIds.keySet().stream().toList());
        for (Link link : foundLinks) {
            updatedLinksMap.put(link, updatedLinkIds.get(link.getId()));
        }
        return updatedLinksMap;
    }
}
