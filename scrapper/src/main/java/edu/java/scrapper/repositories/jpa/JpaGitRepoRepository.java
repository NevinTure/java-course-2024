package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.repositories.GitRepoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGitRepoRepository extends GitRepoRepository, JpaRepository<GitRepository, Long> {
}
