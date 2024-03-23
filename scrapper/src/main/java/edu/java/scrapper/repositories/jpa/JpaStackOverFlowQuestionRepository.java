package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.StackOverFlowQuestion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaStackOverFlowQuestionRepository extends JpaRepository<StackOverFlowQuestion, Long> {

    List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime);

    List<StackOverFlowQuestion> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit);
}
