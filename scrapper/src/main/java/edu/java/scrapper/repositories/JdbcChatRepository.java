package edu.java.scrapper.repositories;

import edu.java.scrapper.model.TgChat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(TgChat tgChat) {
        jdbcTemplate.update("INSERT INTO tg_chat (id) values (?)", tgChat.getId());
    }

    @Override
    public Optional<TgChat> findById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public long deleteById(long id) {
        return 0;
    }
}
