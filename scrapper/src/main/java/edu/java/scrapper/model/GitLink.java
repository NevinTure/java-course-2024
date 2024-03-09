package edu.java.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitLink {
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private OffsetDateTime lastPushAt;

    public GitLink(URI url) {
        this.url = url;
        lastCheckAt = OffsetDateTime.now();
        lastUpdateAt = OffsetDateTime.now();
        lastPushAt = OffsetDateTime.now();
    }
}
