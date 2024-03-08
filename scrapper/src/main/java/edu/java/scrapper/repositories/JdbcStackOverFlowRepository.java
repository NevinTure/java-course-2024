package edu.java.scrapper.repositories;

import edu.java.scrapper.model.StackOverFlowLink;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStackOverFlowRepository implements StackOverFlowLinkRepository {
    @Override
    public void save(StackOverFlowLink link) {

    }

    @Override
    public Optional<StackOverFlowLink> findById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void saveAll(long chatId, List<StackOverFlowLink> links) {

    }

    @Override
    public void deleteByChatId(long chatId) {

    }
}
