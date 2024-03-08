package edu.java.scrapper.model;

import java.net.URI;
import java.time.OffsetDateTime;

public class GitLink {
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private OffsetDateTime lastPushAt;
    private TgChat owner;

}
