package edu.java.scrapper.row_mappers;

import edu.java.scrapper.model.Link;
import org.springframework.jdbc.core.RowMapper;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong("id"));
        link.setUrl(URI.create(rs.getString("url")));
        return link;
    }
}
