package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.GitRepoRepository;
import edu.java.scrapper.services.GitLinkUpdater;
import edu.java.scrapper.services.GitRepositoryService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JdbcGitRepositoryService implements GitRepositoryService {

    private final GitRepoRepository gitRepoRepository;
    private final GitLinkUpdater linkUpdater;
    private static final Pattern REPO_PATTERN = Pattern.compile("https://github\\.com(\\S+)");

    public JdbcGitRepositoryService(GitRepoRepository gitRepoRepository, GitLinkUpdater linkUpdater) {
        this.gitRepoRepository = gitRepoRepository;
        this.linkUpdater = linkUpdater;
    }

    @Override
    public void save(GitRepository repository) {
        gitRepoRepository.save(repository);
    }

    @Override
    public GitRepository createAndSave(Link link) {
        Matcher matcher = REPO_PATTERN.matcher(link.getUrl().toString());
        if (matcher.find()) {
            GitRepository repository = new GitRepository(link.getId(), matcher.group(1));
            linkUpdater.processUpdates(List.of(repository));
            save(repository);
            return repository;
        }
        return null;
    }

    @Override
    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime) {
        return gitRepoRepository.findByLastCheckAtLessThanLimit10(dateTime);
    }

    @Override
    public void batchUpdate(List<GitRepository> repositories) {
        gitRepoRepository.saveAll(repositories);
    }
}
