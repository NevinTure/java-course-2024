package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.GitLink;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class GitLinkRowMapper implements RowMapper<GitLink> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx");
    @Override
    public GitLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        GitLink link = new GitLink();
        link.setId(rs.getLong("id"));
        link.setUrl(URI.create(rs.getString("url")));
        link.setLastCheckAt(OffsetDateTime.parse(rs.getString("last_check_at"), FORMATTER));
        link.setLastUpdateAt(OffsetDateTime.parse(rs.getString("last_update_at"), FORMATTER));
        link.setLastPushAt(OffsetDateTime.parse(rs.getString("last_push_at"), FORMATTER));
        return link;
    }
}
