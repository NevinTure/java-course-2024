package edu.java.bot.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.net.URI;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Link {

    private long id;
    private URI url;

    public Link(long id, URI url) {
        this.id = id;
        this.url = url;
    }
}
