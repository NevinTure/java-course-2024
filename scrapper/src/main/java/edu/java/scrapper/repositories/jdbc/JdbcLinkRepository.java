package edu.java.scrapper.repositories.jdbc;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate =
            new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    @Override
    public Link save(Link link) {
        Long id = jdbcTemplate
            .queryForObject("insert into link (url) values (?) returning id",
                Long.class, link.getUrl().toString());
        link.setId(id);
        return link;
    }

    @Override
    public Optional<Link> findById(long id) {
        List<Link> links = jdbcTemplate
            .query("select * from link where id = ?", new LinkRowMapper(), id);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public boolean existsById(long id) {
        List<Link> links = jdbcTemplate
            .query("select * from link where id = ?", new LinkRowMapper(), id);
        return !links.isEmpty();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from link where id = ?", id);
    }

    @Override
    public Optional<Link> findByUrl(URI uri) {
        List<Link> links = jdbcTemplate
            .query("select * from link where url = ?", new LinkRowMapper(), uri.toString());
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public List<Link> findByIdIn(List<Long> ids) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedJdbcTemplate
            .query("select * from link where id in (:ids)", parameters, new LinkRowMapper());
    }

    @Override
    public List<Long> findLinkFollowerIdsByLinkId(long linkId) {
        return jdbcTemplate.queryForList("select tg_chat_id from tg_chat_link where link_id = ?", Long.class, linkId);
    }
}
