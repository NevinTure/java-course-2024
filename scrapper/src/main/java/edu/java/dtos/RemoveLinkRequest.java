package edu.java.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class RemoveLinkRequest {

    @URL
    private String url;
}
