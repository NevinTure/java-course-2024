package edu.java.bot.dtos.scrapper_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveLinkRequest {

    @URL
    private String url;
}
