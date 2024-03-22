package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.StackOverFlowQuestion;
import edu.java.scrapper.repositories.StackOverFlowQuestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStackOverFlowRepository extends StackOverFlowQuestionRepository, JpaRepository<StackOverFlowQuestion, Long> {
}
