package edu.java.dtos;

import java.net.URI;
import lombok.Data;

@Data
public class LinkResponse {

    private long id;
    private URI url;
}
