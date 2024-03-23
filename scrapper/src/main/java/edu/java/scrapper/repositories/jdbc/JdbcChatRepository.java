package edu.java.scrapper.repositories.jdbc;

import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.row_mappers.TgChatRowMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("MultipleStringLiterals")
public class JdbcChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TgChat save(TgChat tgChat) {
        if (existsById(tgChat.getId())) {
            jdbcTemplate.update("UPDATE tg_chat SET state = ?::state where id = ?",
                tgChat.getState().toString(), tgChat.getId());
        } else {
            jdbcTemplate.update("INSERT INTO tg_chat (id) values (?)", tgChat.getId());
        }
        return tgChat;
    }

    public Optional<TgChat> findById(long id) {
        List<TgChat> tgChats =
            jdbcTemplate.query("select * from tg_chat where id = ?", new TgChatRowMapper(), id);
        if (tgChats.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tgChats.get(0));
    }

    public boolean existsById(long id) {
        return !jdbcTemplate
            .query("select * from tg_chat where id = ?", new TgChatRowMapper(), id)
            .isEmpty();
    }

    public void deleteById(long id) {
        jdbcTemplate.update("delete from tg_chat where id = ?", id);
    }
}
