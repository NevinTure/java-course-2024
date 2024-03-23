package edu.java.scrapper.configuration;

import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.repositories.jpa.JpaGitRepoRepository;
import edu.java.scrapper.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.repositories.jpa.JpaStackOverFlowQuestionRepository;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.GitLinkUpdater;
import edu.java.scrapper.services.GitRepositoryService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.RecognizeLinkService;
import edu.java.scrapper.services.StackOverFlowLinkUpdater;
import edu.java.scrapper.services.StackOverFlowQuestionService;
import edu.java.scrapper.services.jpa.JpaChatLinkService;
import edu.java.scrapper.services.jpa.JpaChatService;
import edu.java.scrapper.services.jpa.JpaGitRepositoryService;
import edu.java.scrapper.services.jpa.JpaLinkService;
import edu.java.scrapper.services.jpa.JpaStackOverFlowQuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfig {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    public JpaConfig(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Bean
    public LinkService linkService() {
        return new JpaLinkService(linkRepository);
    }

    @Bean
    public ChatService chatService() {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public GitRepositoryService gitService(
        JpaGitRepoRepository gitRepoRepository,
        @Lazy GitLinkUpdater gitLinkUpdater
    ) {
        return new JpaGitRepositoryService(gitRepoRepository, gitLinkUpdater);
    }

    @Bean
    public StackOverFlowQuestionService sofService(
        JpaStackOverFlowQuestionRepository sofRepository,
        @Lazy StackOverFlowLinkUpdater sofLinkUpdater
    ) {
        return new JpaStackOverFlowQuestionService(sofRepository, sofLinkUpdater);
    }

    @Bean
    public ChatLinkService chatLinkService(ModelMapper mapper, RecognizeLinkService recognizeLinkService) {
        return new JpaChatLinkService(
            chatService(),
            linkService(),
            recognizeLinkService,
            mapper
        );
    }

}
