package edu.java.scrapper.model;

import edu.java.scrapper.utils.UriConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = {"id", "tgChatList"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = UriConverter.class)
    @Column(name = "url", unique = true)
    private URI url;
    @ManyToMany(mappedBy = "linkList")
    private List<TgChat> tgChatList;

    public Link(URI url) {
        this.url = url;
    }

    public Link(Long id, URI url) {
        this.id = id;
        this.url = url;
    }
}
