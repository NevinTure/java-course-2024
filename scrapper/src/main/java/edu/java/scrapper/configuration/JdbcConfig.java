package edu.java.scrapper.configuration;

import edu.java.scrapper.repositories.ChatLinkRepository;
import edu.java.scrapper.repositories.ChatRepository;
import edu.java.scrapper.repositories.GitRepoRepository;
import edu.java.scrapper.repositories.LinkRepository;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import edu.java.scrapper.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.repositories.jdbc.JdbcChatRepository;
import edu.java.scrapper.repositories.jdbc.JdbcGitRepoRepository;
import edu.java.scrapper.repositories.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repositories.jdbc.JdbcStackOverFlowQuestionRepository;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.GitLinkUpdater;
import edu.java.scrapper.services.RecognizeLinkService;
import edu.java.scrapper.services.StackOverFlowLinkUpdater;
import edu.java.scrapper.services.jdbc.JdbcChatLinkService;
import edu.java.scrapper.services.jdbc.JdbcChatService;
import edu.java.scrapper.services.jdbc.JdbcGitRepositoryService;
import edu.java.scrapper.services.jdbc.JdbcLinkService;
import edu.java.scrapper.services.jdbc.JdbcStackOverFlowQuestionService;
import javax.sql.DataSource;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {

    private final DataSource dataSource;

    public JdbcConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ChatLinkRepository chatLinkRepository() {
        return new JdbcChatLinkRepository(jdbcTemplate());
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JdbcChatRepository(jdbcTemplate());
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JdbcLinkRepository(jdbcTemplate());
    }

    @Bean
    public GitRepoRepository gitRepoRepository() {
        return new JdbcGitRepoRepository(jdbcTemplate());
    }

    @Bean
    public StackOverFlowQuestionRepository sofRepository() {
        return new JdbcStackOverFlowQuestionRepository(jdbcTemplate());
    }

    @Bean
    public JdbcChatService chatService() {
        return new JdbcChatService(chatRepository());
    }

    @Bean
    public JdbcLinkService linkService() {
        return new JdbcLinkService(linkRepository());
    }

    @Bean
    public JdbcStackOverFlowQuestionService sofService(@Lazy StackOverFlowLinkUpdater sofLinkUpdater) {
        return new JdbcStackOverFlowQuestionService(sofRepository(), sofLinkUpdater);
    }

    @Bean
    public JdbcGitRepositoryService gitService(@Lazy GitLinkUpdater gitLinkUpdater) {
        return new JdbcGitRepositoryService(gitRepoRepository(), gitLinkUpdater);
    }

    @Bean
    public ChatLinkService chatLinkService(ModelMapper mapper, RecognizeLinkService recognizeService) {
        return new JdbcChatLinkService(
            chatService(),
            linkService(),
            chatLinkRepository(),
            recognizeService,
            mapper
        );
    }
}
