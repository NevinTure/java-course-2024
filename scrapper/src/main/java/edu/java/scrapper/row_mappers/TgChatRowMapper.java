package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.TgChat;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TgChatRowMapper implements RowMapper<TgChat> {


    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
