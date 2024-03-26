package edu.java.scrapper.services;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.utils.UpdateType;
import java.util.List;
import java.util.Map;

public interface GitLinkUpdater extends LinkUpdater {

    List<UpdateType> processUpdates(List<GitRepository> repositories);

    Map<Long, UpdateType> updateGitRepos(List<GitRepository> repositories);
}
