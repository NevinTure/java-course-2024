package edu.java.scrapper.repositories.jooq;

import edu.java.models.utils.State;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.ChatRepository;
import java.util.List;
import java.util.Optional;
import edu.java.scrapper.row_mappers.TgChatRowMapper;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import static edu.java.scrapper.model.jooq.Tables.TG_CHAT;
import static edu.java.scrapper.model.jooq.Tables.TG_CHAT_LINK;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.val;

public class JooqChatRepository implements ChatRepository {

    private final DSLContext context;

    public JooqChatRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public TgChat save(TgChat tgChat) {
        if (existsById(tgChat.getId())) {
            context
                .update(TG_CHAT)
                .set(TG_CHAT.STATE
                    .convertTo(State.class, v -> edu.java.scrapper.model.jooq.enums.State.valueOf(v.name())), tgChat.getState())
                .where(TG_CHAT.ID.eq(tgChat.getId())).execute();
        } else {
            context
                .insertInto(TG_CHAT, TG_CHAT.ID)
                .values(tgChat.getId()).execute();
        }
        return tgChat;
    }

    @Override
    public Optional<TgChat> findById(long id) {
        List<TgChat> tgChats = context
            .select(TG_CHAT.ID, TG_CHAT.STATE.convertFrom(State.class, v -> State.valueOf(v.getLiteral())))
            .from(TG_CHAT).where(TG_CHAT.ID.eq(id)).fetch(Records.mapping((v1, v2) -> {
                TgChat chat = new TgChat();
                chat.setId(v1);
                chat.setState(v2);
                return chat;
            }));
        if (tgChats.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tgChats.get(0));
    }

    @Override
    public boolean existsById(long id) {
        return !context
            .select().from(TG_CHAT)
            .where(TG_CHAT.ID.eq(id)).fetchInto(TgChat.class).isEmpty();
    }

    @Override
    public void deleteById(long id) {
        context.delete(TG_CHAT).where(TG_CHAT.ID.eq(id)).execute();
    }
}
