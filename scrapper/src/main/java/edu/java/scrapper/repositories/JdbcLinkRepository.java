package edu.java.scrapper.repositories;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate =
            new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    @Override
    public Integer save(Link link) {
        return jdbcTemplate
            .queryForObject("insert into link (url) values (?) returning id",
                Integer.class, link.getUrl().toString());
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
    public List<Link> findLinkByIds(List<Long> ids) {
        Map<String, List<Long>> parameters =
            Map.of("ids", ids);
        return namedJdbcTemplate
            .query("select * from link where id in (:ids)", parameters, new LinkRowMapper());
    }
}
