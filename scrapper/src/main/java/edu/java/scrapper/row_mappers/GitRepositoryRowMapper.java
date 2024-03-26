package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.GitRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;

public class GitRepositoryRowMapper implements RowMapper<GitRepository> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx");

    @Override
    public GitRepository mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GitRepository(
            rs.getLong("id"),
            rs.getLong("link_id"),
            rs.getString("urn"),
            OffsetDateTime.parse(rs.getString("last_check_at"), FORMATTER),
            OffsetDateTime.parse(rs.getString("last_update_at"), FORMATTER),
            OffsetDateTime.parse(rs.getString("last_push_at"), FORMATTER)
        );
    }
}
