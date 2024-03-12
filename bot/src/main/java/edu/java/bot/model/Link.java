package edu.java.bot.model;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Link {

    private long id;
    private URI url;

    public Link(URI url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
