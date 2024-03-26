package edu.java.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateRequest {

    @Min(0)
    private long id;
    @URL
    private String url;
    private String description;
    @Size(min = 1)
    private List<Long> tgChatIds;
}
