package edu.java.scrapper.model;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;

public class StackOverFlowLink {
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private Integer answers;
    private TgChat owner;
}
