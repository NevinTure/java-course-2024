package edu.java.bot.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Person {

    private Long id;
    private boolean waitingTrack;
    private boolean waitingUntrack;
    private List<String> linkList;

    public Person(Long id) {
        this.id = id;
        linkList = new ArrayList<>();
    }
}
