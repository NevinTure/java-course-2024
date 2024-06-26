package edu.java.bot.model;

import edu.java.models.utils.State;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TgChat {

    private Long id;
    private State state;

    public TgChat(Long id) {
        this.id = id;
        state = State.DEFAULT;
    }
}
