package edu.java.bot.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdate {
    private long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
