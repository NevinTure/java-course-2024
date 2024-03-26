package edu.java.scrapper.repositories.jooq;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChatLink;
import edu.java.scrapper.repositories.ChatLinkRepository;
import edu.java.scrapper.row_mappers.LinkRowMapper;
import edu.java.scrapper.row_mappers.TgChatLinkRowMapper;
import org.jooq.DSLContext;
import java.util.List;
import static edu.java.scrapper.model.jooq.Tables.LINK;
import static edu.java.scrapper.model.jooq.Tables.TG_CHAT_LINK;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.delete;

public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext context;

    public JooqChatLinkRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void addLink(long chatId, long linkId) {
        context
            .insertInto(TG_CHAT_LINK, TG_CHAT_LINK.TG_CHAT_ID, TG_CHAT_LINK.LINK_ID)
                .values(chatId, linkId)
            .execute();
    }

    @Override
    public void removeLink(long chatId, long linkId) {
        context
            .delete(TG_CHAT_LINK)
            .where(TG_CHAT_LINK.TG_CHAT_ID.eq(chatId)).and(TG_CHAT_LINK.LINK_ID.eq(linkId))
            .execute();
    }

    @Override
    public List<Link> getLinksByChatId(long chatId) {
        return context
            .select(LINK.ID, LINK.URL)
            .from(TG_CHAT_LINK).leftJoin(LINK).on(TG_CHAT_LINK.LINK_ID.eq(LINK.ID))
            .where(TG_CHAT_LINK.TG_CHAT_ID.eq(chatId)).fetchInto(Link.class);
    }

    @Override
    public void deleteChatRelatedLinks(long chatId) {
        context
            .delete(TG_CHAT_LINK)
                .where(TG_CHAT_LINK.TG_CHAT_ID.eq(chatId))
                    .execute();
    }

    @Override
    public boolean existsByChatAndLinkId(long chatId, long linkId) {

        List<TgChatLink> tgChatLinks = context
            .select().from(TG_CHAT_LINK)
            .where(TG_CHAT_LINK.TG_CHAT_ID.eq(chatId)).and(TG_CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(TgChatLink.class);
        return !tgChatLinks.isEmpty();
    }
}
