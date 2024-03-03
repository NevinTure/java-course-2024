package edu.java.scrapper.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TgChat {

    private Long id;
    private List<Link> linkList;

    public TgChat(Long id) {
        this.id = id;
        linkList = new ArrayList<>();
    }
}
