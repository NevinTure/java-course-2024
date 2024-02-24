package edu.java.bot.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TgChat {

    private Long id;
    private boolean waitingTrack;
    private boolean waitingUntrack;
    private List<String> linkList;

    public TgChat(Long id) {
        this.id = id;
        linkList = new ArrayList<>();
    }
}
