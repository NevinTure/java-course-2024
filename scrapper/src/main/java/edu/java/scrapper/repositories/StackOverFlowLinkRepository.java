package edu.java.scrapper.repositories;

import edu.java.scrapper.model.GitLink;
import edu.java.scrapper.model.StackOverFlowLink;
import java.util.List;
import java.util.Optional;

public interface StackOverFlowLinkRepository {

    void save(StackOverFlowLink link);

    Optional<StackOverFlowLink> findById(long id);

    boolean existsById(long id);

    void deleteById(long id);
}
