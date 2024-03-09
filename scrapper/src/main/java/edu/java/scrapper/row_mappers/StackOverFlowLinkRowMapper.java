package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.StackOverFlowLink;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class StackOverFlowLinkRowMapper implements RowMapper<StackOverFlowLink> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx");

    @Override
    public StackOverFlowLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        StackOverFlowLink link = new StackOverFlowLink();
        link.setId(rs.getLong("id"));
        link.setAnswers(rs.getInt("answers"));
        link.setUrl(URI.create(rs.getString("url")));
        link.setLastCheckAt(OffsetDateTime.parse(rs.getString("last_check_at"), FORMATTER));
        link.setLastUpdateAt(OffsetDateTime.parse(rs.getString("last_update_at"), FORMATTER));
        return link;
    }
}
