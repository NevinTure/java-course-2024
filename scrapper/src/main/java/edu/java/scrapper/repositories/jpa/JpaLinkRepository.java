package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaLinkRepository extends LinkRepository, JpaRepository<Link, Long> {

    @Query(value = "select tg_chat_id from tg_chat_link where link_id = :linkId", nativeQuery = true)
    List<Long> findLinkFollowerIdsByLinkId(@Param("linkId") Long linkId);
}
