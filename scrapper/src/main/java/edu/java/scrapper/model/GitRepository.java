package edu.java.scrapper.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "git_repository")
public class GitRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "link_id")
    private long linkId;
    private String urn;
    @Column(name = "last_check_at")
    private OffsetDateTime lastCheckAt;
    @Column(name = "last_update_at")
    private OffsetDateTime lastUpdateAt;
    @Column(name = "last_push_at")
    private OffsetDateTime lastPushAt;

    public GitRepository(long linkId, String urn) {
        this.linkId = linkId;
        this.urn = urn;
        lastCheckAt = OffsetDateTime.now().withNano(0);
        lastUpdateAt = OffsetDateTime.now().withNano(0);
        lastPushAt = OffsetDateTime.now().withNano(0);
    }
}
