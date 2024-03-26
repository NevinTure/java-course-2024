package edu.java.scrapper.repositories.jooq;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import static edu.java.scrapper.model.jooq.Tables.LINK;
import static edu.java.scrapper.model.jooq.Tables.TG_CHAT_LINK;

public class JooqLinkRepository implements LinkRepository {

    private final DSLContext context;

    public JooqLinkRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Link save(Link link) {
        Record1<Long> idRecord = context
            .insertInto(LINK, LINK.URL).values(link.getUrl().toString()).returningResult(LINK.ID).fetchOne();
        link.setId(idRecord.component1());
        return link;
    }

    @Override
    public Optional<Link> findById(long id) {
        List<Link> links = context
            .select().from(LINK).where(LINK.ID.eq(id)).fetchInto(Link.class);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public boolean existsById(long id) {
        List<Link> links = context
            .select().from(LINK).where(LINK.ID.eq(id)).fetchInto(Link.class);
        return !links.isEmpty();
    }

    @Override
    public void deleteById(long id) {
        context
            .delete(LINK).where(LINK.ID.eq(id)).execute();
    }

    @Override
    public Optional<Link> findByUrl(URI uri) {
        List<Link> links = context
            .select().from(LINK).where(LINK.URL.eq(uri.toString())).fetchInto(Link.class);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public List<Link> findByIdIn(List<Long> ids) {
        return context
            .select().from(LINK).where(LINK.ID.in(ids)).fetchInto(Link.class);
    }

    @Override
    public List<Long> findLinkFollowerIdsByLinkId(long linkId) {
        return context
            .select(TG_CHAT_LINK.TG_CHAT_ID).from(TG_CHAT_LINK)
            .where(TG_CHAT_LINK.LINK_ID.eq(linkId)).fetchInto(Long.class);
    }
}
