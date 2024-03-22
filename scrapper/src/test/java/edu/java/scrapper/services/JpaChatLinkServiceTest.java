package edu.java.scrapper.services;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.repositories.jpa.JpaLinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class JpaChatLinkServiceTest extends IntegrationEnvironment {

    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JpaChatLinkServiceTest(JpaChatRepository chatRepository, JpaLinkRepository linkRepository,
        JdbcTemplate jdbcTemplate
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    public void test() {
        jdbcTemplate.update("insert into tg_chat (id, state) VALUES (1, 'DEFAULT')");
        jdbcTemplate.update("insert into tg_chat (id, state) VALUES (2, 'DEFAULT')");
        jdbcTemplate.update("insert into link (url) values ('url1')");
        jdbcTemplate.update("insert into tg_chat_link (tg_chat_id, link_id) values (1, 1), (2, 1)");
        System.out.println(linkRepository.findLinkFollowerIdsByLinkId(1L));
    }
}
