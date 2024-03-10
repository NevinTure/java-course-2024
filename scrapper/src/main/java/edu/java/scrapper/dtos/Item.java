package edu.java.scrapper.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.Data;

@Data
public class Item {

    private OffsetDateTime dateTime;
    @JsonProperty("answer_count")
    private int answerCount;

    @JsonProperty("last_activity_date")
    private void parseDate(long seconds) {
        dateTime = OffsetDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneOffset.UTC);
    }
}
