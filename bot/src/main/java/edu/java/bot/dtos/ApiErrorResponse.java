package edu.java.bot.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private String[] stackTrace;

}
