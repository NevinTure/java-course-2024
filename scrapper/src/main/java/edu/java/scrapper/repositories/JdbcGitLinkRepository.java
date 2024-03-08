package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitLink;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.row_mappers.GitLinkRowMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JdbcGitLinkRepository implements GitLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ChatRepository chatRepository;

    public JdbcGitLinkRepository(JdbcTemplate jdbcTemplate,@Lazy ChatRepository chatRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.chatRepository = chatRepository;
    }

    @Override
    public void save(GitLink link) {
        if (existsById(link.getId())) {
            jdbcTemplate.update("update git_link set last_check_at = ?, last_push_at = ?, last_update_at = ? where id = ?",
                link.getLastCheckAt(), link.getLastPushAt(), link.getLastUpdateAt(), link.getId());
        } else {
            jdbcTemplate
                .update("insert into git_link (tg_chat_id, url, last_check_at, last_update_at, last_push_at) values (?, ?, ?, ?, ?)",
                    link.getId(), link.getUrl(), link.getLastCheckAt(), link.getLastUpdateAt(), link.getLastPushAt());
        }
    }

    @Override
    public Optional<GitLink> findById(long id) {
        List<GitLink> links = jdbcTemplate
            .query("select * from git_link inner join tg_chat on git_link.tg_chat_id = tg_chat.id where git_link.id = ?", new GitLinkRowMapper(), id);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.get(0));
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void saveAll(long chatId, List<GitLink> links) {
    }

    @Override
    public void deleteByChatId(long chatId) {

    }

    private TgChat findOwner(long id) {
        return chatRepository.findById(id).get();
    }
}
