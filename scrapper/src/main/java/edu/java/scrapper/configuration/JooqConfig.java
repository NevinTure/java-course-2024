package edu.java.scrapper.configuration;

import edu.java.scrapper.repositories.ChatLinkRepository;
import edu.java.scrapper.repositories.ChatRepository;
import edu.java.scrapper.repositories.GitRepoRepository;
import edu.java.scrapper.repositories.LinkRepository;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import edu.java.scrapper.repositories.jooq.JooqChatLinkRepository;
import edu.java.scrapper.repositories.jooq.JooqChatRepository;
import edu.java.scrapper.repositories.jooq.JooqGitRepoRepository;
import edu.java.scrapper.repositories.jooq.JooqLinkRepository;
import edu.java.scrapper.repositories.jooq.JooqStackOverFlowQuestionRepository;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.GitLinkUpdater;
import edu.java.scrapper.services.RecognizeLinkService;
import edu.java.scrapper.services.StackOverFlowLinkUpdater;
import edu.java.scrapper.services.jdbc.JdbcChatLinkService;
import edu.java.scrapper.services.jdbc.JdbcChatService;
import edu.java.scrapper.services.jdbc.JdbcGitRepositoryService;
import edu.java.scrapper.services.jdbc.JdbcLinkService;
import edu.java.scrapper.services.jdbc.JdbcStackOverFlowQuestionService;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqConfig {

    private final DataSource dataSource;

    @Autowired
    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DSLContext context() {
        try {
            return DSL
                .using(dataSource.getConnection(),
                    new Settings().withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ChatLinkRepository chatLinkRepository() {
        return new JooqChatLinkRepository(context());
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JooqChatRepository(context());
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JooqLinkRepository(context());
    }

    @Bean
    public GitRepoRepository gitRepoRepository() {
        return new JooqGitRepoRepository(context());
    }

    @Bean
    public StackOverFlowQuestionRepository sofRepository() {
        return new JooqStackOverFlowQuestionRepository(context());
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
