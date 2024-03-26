package edu.java.scrapper.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        lastCheckAt = OffsetDateTime.now().withNano(0);
        lastUpdateAt = OffsetDateTime.now().withNano(0);
        answers = 0;
    }
}
