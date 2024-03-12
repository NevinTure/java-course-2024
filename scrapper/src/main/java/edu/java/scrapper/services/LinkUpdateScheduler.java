package edu.java.scrapper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class LinkUpdateScheduler {

    private final GitLinkUpdater gitLinkUpdater;
    private final StackOverFlowLinkUpdater sofLinkUpdater;

    public LinkUpdateScheduler(GitLinkUpdater gitLinkUpdater, StackOverFlowLinkUpdater sofLinkUpdater) {
        this.gitLinkUpdater = gitLinkUpdater;
        this.sofLinkUpdater = sofLinkUpdater;
    }

    @Scheduled(fixedDelayString =
        "#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler().forceCheckDelay()}")
    public void update() {
//        gitLinkUpdater.update();
//        sofLinkUpdater.update();
    }
}
