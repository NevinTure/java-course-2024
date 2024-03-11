package edu.java.scrapper.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TgChat {

    private Long id;
    private State state;
    private List<Link> linkList;

    public TgChat(Long id) {
        this.id = id;
        linkList = new ArrayList<>();
    }
}
