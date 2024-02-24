package edu.java.bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdate {
    private long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
