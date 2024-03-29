package edu.java.scrapper.row_mappers;

import edu.java.models.utils.State;
import edu.java.scrapper.model.TgChat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class TgChatRowMapper implements RowMapper<TgChat> {

    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        TgChat chat = new TgChat();
        chat.setId(rs.getLong("id"));
        chat.setState(State.valueOf(rs.getString("state").toUpperCase()));
        return chat;
    }
}
