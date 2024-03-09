package edu.java.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackOverFlowLink {
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private Integer answers;
}
