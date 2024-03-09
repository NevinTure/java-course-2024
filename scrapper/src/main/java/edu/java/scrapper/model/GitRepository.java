package edu.java.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitRepository {

    private Long id;
//    private Link relatedLink;
    private long linkId;
    private String urn;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private OffsetDateTime lastPushAt;

    public GitRepository(long linkId, String urn) {
        this.linkId = linkId;
        this.urn = urn;
        lastCheckAt = OffsetDateTime.now();
        lastUpdateAt = OffsetDateTime.now();
        lastPushAt = OffsetDateTime.now();
    }
}
