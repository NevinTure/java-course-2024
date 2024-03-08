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

    private final GitLinkRepository gitLinkRepository;
    private final StackOverFlowLinkRepository sofLinkRepository;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate,
        GitLinkRepository gitLinkRepository, StackOverFlowLinkRepository sofLinkRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.gitLinkRepository = gitLinkRepository;
        this.sofLinkRepository = sofLinkRepository;
    }

    @Override
    public void save(TgChat tgChat) {
        Optional<TgChat> foundChatOp = findById(tgChat.getId());
        if (foundChatOp.isPresent()) {
            TgChat foundChat = foundChatOp.get();
            List<GitLink> newGitLinks = getDistinctGitLinks(tgChat.getGitLinks(), foundChat.getGitLinks());
            List<StackOverFlowLink> newSofLinks =
                getDistinctSofLinks(tgChat.getStackOverFlowLinks(), foundChat.getStackOverFlowLinks());
            gitLinkRepository.saveAll(tgChat.getId(), newGitLinks);
            sofLinkRepository.saveAll(tgChat.getId(), newSofLinks);
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
        TgChat chat = tgChats.get(0);
        chat.setGitLinks(getRelatedGitLinks(id));
        chat.setStackOverFlowLinks(getRelatedSofLinks(id));
        return Optional.of(chat);
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
        gitLinkRepository.deleteByChatId(id);
        sofLinkRepository.deleteByChatId(id);
    }

    private List<GitLink> getRelatedGitLinks(long id) {
        return jdbcTemplate.query(
            "select * from git_link where tg_chat_id = ?",
            new GitLinkRowMapper(),
            id
        );
    }

    private List<StackOverFlowLink> getRelatedSofLinks(long id) {
        return jdbcTemplate.query(
            "select * from stackoverflow_link where tg_chat_id = ?",
            new StackOverFlowLinkRowMapper(),
            id
        );
    }

    private List<GitLink> getDistinctGitLinks(List<GitLink> l1, List<GitLink> l2) {
        return l1.stream().filter(v -> !l2.contains(v)).toList();
    }

    private List<StackOverFlowLink> getDistinctSofLinks(
        List<StackOverFlowLink> l1,
        List<StackOverFlowLink> l2) {
        return l1.stream().filter(v -> !l2.contains(v)).toList();
    }
}
