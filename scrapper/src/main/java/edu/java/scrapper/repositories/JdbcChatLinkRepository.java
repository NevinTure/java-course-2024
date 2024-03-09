package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChatLink;
import edu.java.scrapper.row_mappers.TgChatLinkRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatLinkRepository(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLink(long chatId, long linkId) {
        jdbcTemplate.update("insert into tg_chat_link (tg_chat_id, link_id) values (?, ?)", chatId, linkId);

    }

    @Override
    public void removeLink(long chatId, long linkId) {
        jdbcTemplate.update("delete from tg_chat_link where tg_chat_id = ? and link_id = ?", chatId, linkId);
    }

    @Override
    public void deleteChatRelatedLinks(long chatId) {
        jdbcTemplate.update("delete from tg_chat_link where tg_chat_id = ?", chatId);
    }

    @Override
    public boolean existsByChatAndLinkId(long chatId, long linkId) {
        List<TgChatLink> tgChatLinks = jdbcTemplate
            .query("select * from tg_chat_link where tg_chat_id = ? and link_id = ?",
                new TgChatLinkRowMapper(),
                chatId, linkId);
        return !tgChatLinks.isEmpty();
    }

}
