package edu.java.scrapper.configuration;

import javax.sql.DataSource;
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
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.RecognizeLinkService;
import edu.java.scrapper.services.jdbc.JdbcChatLinkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {

    private final DataSource dataSource;
    private final ModelMapper mapper;
    private final RecognizeLinkService recognizeService;
    private final ChatService chatService;
    private final LinkService linkService;

    @Autowired
    public JdbcConfig(DataSource dataSource, ModelMapper mapper, RecognizeLinkService recognizeService,
        ChatService chatService,
        LinkService linkService
    ) {
        this.dataSource = dataSource;
        this.mapper = mapper;
        this.recognizeService = recognizeService;
        this.chatService = chatService;
        this.linkService = linkService;
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
    public ChatLinkService chatLinkService() {
        return new JdbcChatLinkService(
            chatService,
            linkService,
            chatLinkRepository(),
            recognizeService,
            mapper
        );
    }
}
