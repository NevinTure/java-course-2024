package edu.java.bot.dtos;

import lombok.Data;
import java.util.List;

@Data
public class LinkUpdateRequest {

    private long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
