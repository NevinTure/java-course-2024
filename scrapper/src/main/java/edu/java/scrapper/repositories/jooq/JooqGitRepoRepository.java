package edu.java.scrapper.repositories.jooq;

import edu.java.scrapper.model.GitRepository;
import edu.java.scrapper.repositories.GitRepoRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Records;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.model.jooq.Tables.GIT_REPOSITORY;

public class JooqGitRepoRepository implements GitRepoRepository {

    private final DSLContext context;

    public JooqGitRepoRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public GitRepository save(GitRepository repo) {
        Record1<Long> idRecord = context
            .insertInto(GIT_REPOSITORY,
                GIT_REPOSITORY.LINK_ID, GIT_REPOSITORY.URN, GIT_REPOSITORY.LAST_CHECK_AT, GIT_REPOSITORY.LAST_UPDATE_AT,
                GIT_REPOSITORY.LAST_PUSH_AT
            ).values(repo.getLinkId(), repo.getUrn(), repo.getLastCheckAt(),
                repo.getLastUpdateAt(), repo.getLastPushAt()
            ).returningResult(GIT_REPOSITORY.ID).fetchOne();
        repo.setId(idRecord.component1());
        return repo;
    }

    @Override
    public void deleteById(long id) {
        context
            .delete(GIT_REPOSITORY).where(GIT_REPOSITORY.ID.eq(id)).execute();
    }

    @Override
    public List<GitRepository> findAll() {
        return context
            .select(
                GIT_REPOSITORY.ID,
                GIT_REPOSITORY.LINK_ID,
                GIT_REPOSITORY.URN,
                GIT_REPOSITORY.LAST_CHECK_AT,
                GIT_REPOSITORY.LAST_UPDATE_AT,
                GIT_REPOSITORY.LAST_PUSH_AT
            ).from(GIT_REPOSITORY).fetch().map(Records.mapping(this::mapper));
    }

    @Override
    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime) {
        return context
            .select(
                GIT_REPOSITORY.ID,
                GIT_REPOSITORY.LINK_ID,
                GIT_REPOSITORY.URN,
                GIT_REPOSITORY.LAST_CHECK_AT,
                GIT_REPOSITORY.LAST_UPDATE_AT,
                GIT_REPOSITORY.LAST_PUSH_AT
            ).from(GIT_REPOSITORY).where(GIT_REPOSITORY.LAST_CHECK_AT.le(dateTime))
            .fetch().map(Records.mapping(this::mapper));
    }

    @Override
    public List<GitRepository> findByLastCheckAtLessThan(OffsetDateTime dateTime, Limit limit) {
        return context
            .select(
                GIT_REPOSITORY.ID,
                GIT_REPOSITORY.LINK_ID,
                GIT_REPOSITORY.URN,
                GIT_REPOSITORY.LAST_CHECK_AT,
                GIT_REPOSITORY.LAST_UPDATE_AT,
                GIT_REPOSITORY.LAST_PUSH_AT
            ).from(GIT_REPOSITORY).where(GIT_REPOSITORY.LAST_CHECK_AT.le(dateTime))
            .limit(limit.max()).fetch().map(Records.mapping(this::mapper));
    }

    @Override
    @SuppressWarnings("MagicNumber")
    @Transactional
    public void saveAll(List<GitRepository> repositories) {
        context
            .batched(c -> {
                for (GitRepository repo : repositories) {
                    c.dsl().update(GIT_REPOSITORY)
                        .set(GIT_REPOSITORY.URN, repo.getUrn())
                        .set(GIT_REPOSITORY.LAST_CHECK_AT, repo.getLastCheckAt())
                        .set(GIT_REPOSITORY.LAST_UPDATE_AT, repo.getLastUpdateAt())
                        .set(GIT_REPOSITORY.LAST_PUSH_AT, repo.getLastPushAt())
                        .where(GIT_REPOSITORY.ID.eq(repo.getId()))
                        .execute();
                }
            });
    }

    private GitRepository mapper(
        Long id,
        Long linkId,
        String urn,
        OffsetDateTime lastCheckAt,
        OffsetDateTime lastUpdateAt,
        OffsetDateTime lastPushAt
    ) {
        GitRepository repository = new GitRepository();
        repository.setId(id);
        repository.setUrn(urn);
        repository.setLinkId(linkId);
        repository.setLastCheckAt(lastCheckAt);
        repository.setLastUpdateAt(lastUpdateAt);
        repository.setLastPushAt(lastPushAt);
        return repository;
    }
}
