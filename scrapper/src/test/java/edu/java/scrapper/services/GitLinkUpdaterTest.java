package edu.java.scrapper.services;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.clients.GitHubClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class GitLinkUpdaterTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;
    @Autowired
    private GitRepositoryService service;
    @Autowired
    private GitLinkUpdater linkUpdater;
    @MockBean
    private GitHubClient client;
    @MockBean
    private LinkUpdateSenderService linkUpdateSenderService;

    @Test
    public void testUpdate() {

    }
}
