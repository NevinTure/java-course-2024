package edu.java.scrapper.repositories.jdbc;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChatLink;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import edu.java.scrapper.row_mappers.TgChatLinkRowMapper;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcChatLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatLinkRepository(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLink(long chatId, long linkId) {
        jdbcTemplate.update("insert into tg_chat_link (tg_chat_id, link_id) values (?, ?)", chatId, linkId);

    }

    public void removeLink(long chatId, long linkId) {
        jdbcTemplate.update("delete from tg_chat_link where tg_chat_id = ? and link_id = ?", chatId, linkId);
    }

    public List<Link> getLinksByChatId(long chatId) {
        return jdbcTemplate
            .query(
                "select id, url from tg_chat_link left join link on link_id = id where tg_chat_id = ?",
                new LinkRowMapper(),
                chatId
            );
    }

    public void deleteChatRelatedLinks(long chatId) {
        jdbcTemplate.update("delete from tg_chat_link where tg_chat_id = ?", chatId);
    }

    public boolean existsByChatAndLinkId(long chatId, long linkId) {
        List<TgChatLink> tgChatLinks = jdbcTemplate
            .query("select * from tg_chat_link where tg_chat_id = ? and link_id = ?",
                new TgChatLinkRowMapper(),
                chatId, linkId);
        return !tgChatLinks.isEmpty();
    }
}
