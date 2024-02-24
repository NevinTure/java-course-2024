package edu.java.bot.dtos;

import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class LinkUpdateRequest {

    private long id;
    @URL
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
