package edu.java.dtos;

import java.util.List;
import lombok.Data;

@Data
public class ListLinksResponse {

    private List<LinkResponse> links;
}
