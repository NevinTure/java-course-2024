package edu.java.scrapper.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class GitHubResponse {

    @JsonProperty("id")
    long id;

    @JsonProperty("type")
    String type;

    @JsonProperty("created_at")
    OffsetDateTime dateTime;

}
