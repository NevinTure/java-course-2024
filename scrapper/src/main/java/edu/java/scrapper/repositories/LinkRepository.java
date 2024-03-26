package edu.java.scrapper.repositories;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface LinkRepository {

    public Link save(Link link);

    public Optional<Link> findById(long id);

    public boolean existsById(long id);

    public void deleteById(long id);

    public Optional<Link> findByUrl(URI uri);

    public List<Link> findByIdIn(List<Long> ids);

    public List<Long> findLinkFollowerIdsByLinkId(long linkId);
}
