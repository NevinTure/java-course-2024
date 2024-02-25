package edu.java.bot.model;

import java.net.URI;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
