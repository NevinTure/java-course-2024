package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.StackOverFlowQuestion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;

public class StackOverFlowQuestionRowMapper implements RowMapper<StackOverFlowQuestion> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx");

    @Override
    public StackOverFlowQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StackOverFlowQuestion(
            rs.getLong("id"),
            rs.getLong("link_id"),
            rs.getString("urn"),
            OffsetDateTime.parse(rs.getString("last_check_at"), FORMATTER),
            OffsetDateTime.parse(rs.getString("last_update_at"), FORMATTER),
            rs.getInt("answers")
        );
    }
}
