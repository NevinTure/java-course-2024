package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.ChatRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends ChatRepository, JpaRepository<TgChat, Long> {
}
