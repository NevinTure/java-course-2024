package edu.java.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StackOverFlowQuestion {

    private Long id;
//    private Link relatedLink;
    private long linkId;
    private String urn;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private Integer answers;

    public StackOverFlowQuestion(long linkId, String urn) {
        this.linkId = linkId;
        this.urn = urn;
        lastCheckAt = OffsetDateTime.now();
        lastUpdateAt = OffsetDateTime.now();
        answers = 0;
    }
}
