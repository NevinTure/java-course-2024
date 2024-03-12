package edu.java.scrapper.services;

import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GitRepositoryServiceTest extends IntegrationEnvironment {

    private final LinkService linkService;
    private final GitRepositoryService service;

    @Autowired
    public GitRepositoryServiceTest(LinkService linkService, GitRepositoryService service) {
        this.linkService = linkService;
        this.service = service;
    }

//    @Test
//    public void testCreateAndSave() {
//        //given
//        int linkId = linkService.save();
//    }
}
