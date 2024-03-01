package edu.java.scrapper.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class StackOverflowResponse {

    @JsonProperty("items")
    List<Item> items;
}
