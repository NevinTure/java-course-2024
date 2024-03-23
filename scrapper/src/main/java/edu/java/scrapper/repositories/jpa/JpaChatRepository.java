package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.TgChat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaChatRepository extends JpaRepository<TgChat, Long> {
}
