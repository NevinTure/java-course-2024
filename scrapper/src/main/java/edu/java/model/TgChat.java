package edu.java.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TgChat {

    private Long id;
    private List<String> linkList;

    public TgChat(Long id) {
        this.id = id;
        linkList = new ArrayList<>();
    }
}
