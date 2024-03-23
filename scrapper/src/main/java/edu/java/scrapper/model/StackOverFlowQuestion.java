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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "stackoverflow_question")
public class StackOverFlowQuestion {

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
    private Integer answers;

    public StackOverFlowQuestion(long linkId, String urn) {
        this.linkId = linkId;
        this.urn = urn;
        lastCheckAt = OffsetDateTime.now().withNano(0);
        lastUpdateAt = OffsetDateTime.now().withNano(0);
        answers = 0;
    }
}
