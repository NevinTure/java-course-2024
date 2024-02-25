package edu.java.model;

import java.net.URI;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Link {

    private long id;
    private URI url;
}
