package edu.java.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.Data;

@Data
public class Item {

    OffsetDateTime dateTime;

    @JsonProperty("last_activity_date")
    private void parseDate(long seconds) {
        dateTime = OffsetDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneOffset.UTC);
    }
}
