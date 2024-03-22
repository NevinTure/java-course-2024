package edu.java.scrapper.configuration;

import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.RecognizeLinkService;
import edu.java.scrapper.services.jpa.JpaChatLinkService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@EnableJpaRepositories("edu.java.scrapper.repositories.jpa")
public class JpaConfig {

    private final ChatService chatService;
    private final LinkService linkService;
    private final RecognizeLinkService recognizeLinkService;
    private final ModelMapper mapper;

    public JpaConfig(
        ChatService chatService,
        LinkService linkService,
        RecognizeLinkService recognizeLinkService,
        ModelMapper mapper
    ) {
        this.chatService = chatService;
        this.linkService = linkService;
        this.recognizeLinkService = recognizeLinkService;
        this.mapper = mapper;
    }

    @Bean
    public ChatLinkService chatLinkService() {
        return new JpaChatLinkService(
            chatService,
            linkService,
            recognizeLinkService,
            mapper
        );
    }
}
