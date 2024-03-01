package edu.java.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateRequest {

    private long id;
    @URL
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
