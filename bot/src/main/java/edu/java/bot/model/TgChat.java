package edu.java.bot.model;

import edu.java.models.utils.State;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TgChat {

    private Long id;
    private State state;
    private List<Link> linkList;

    public TgChat(Long id) {
        this.id = id;
        state = State.DEFAULT;
        linkList = new ArrayList<>();
    }
}
