package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.TgChatLink;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TgChatLinkRowMapper implements RowMapper<TgChatLink> {
    @Override
    public TgChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TgChatLink(rs.getLong("tg_chat_id"), rs.getLong("link_id"));
    }
}
