package edu.java.bot.model;

import lombok.Data;
import java.util.List;

@Data
public class LinkUpdate {
    private long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
