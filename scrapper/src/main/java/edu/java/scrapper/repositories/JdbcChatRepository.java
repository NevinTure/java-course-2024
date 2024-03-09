package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitLink;
import edu.java.scrapper.model.StackOverFlowLink;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.row_mappers.GitLinkRowMapper;
import edu.java.scrapper.row_mappers.StackOverFlowLinkRowMapper;
import edu.java.scrapper.row_mappers.TgChatRowMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(TgChat tgChat) {
        Optional<TgChat> foundChatOp = findById(tgChat.getId());
        if (foundChatOp.isPresent()) {
            jdbcTemplate.update("UPDATE tg_chat SET state = ? where id = ?",
                tgChat.getState(), tgChat.getId());
        } else {
            jdbcTemplate.update("INSERT INTO tg_chat (id) values (?)", tgChat.getId());
        }
    }

    @Override
    public Optional<TgChat> findById(long id) {
        List<TgChat> tgChats =
            jdbcTemplate.query("select * from tg_chat where id = ?", new TgChatRowMapper(), id);
        if (tgChats.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tgChats.get(0));
    }

    @Override
    public boolean existsById(long id) {
        return !jdbcTemplate
            .query("select * from tg_chat where id = ?", new TgChatRowMapper(), id)
            .isEmpty();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from tg_chat where id = ?", id);
    }
}
