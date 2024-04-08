package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByIdIn(List<Long> ids);

    Optional<Link> findByUrl(URI url);

    @Query(value = "select tg_chat_id from tg_chat_link where link_id = ?1", nativeQuery = true)
    List<Long> findLinkFollowerIdsByLinkId(Long id);
}
