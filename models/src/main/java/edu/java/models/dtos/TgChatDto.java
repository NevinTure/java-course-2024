package edu.java.models.dtos;

import edu.java.models.utils.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgChatDto {

    private Long id;
    private State state;
}
