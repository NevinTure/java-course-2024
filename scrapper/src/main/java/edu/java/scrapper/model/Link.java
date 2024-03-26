package edu.java.scrapper.model;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Link {

    private long id;
    private URI url;

    public Link(URI url) {
        this.url = url;
    }
}
