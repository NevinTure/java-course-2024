package edu.java.scrapper.configuration;

import edu.java.models.exceptions.ApiBadRequestException;
import edu.java.models.exceptions.ApiNotFoundException;
import edu.java.models.utils.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandledExceptionConfig {

    @Bean
    public List<Class<? extends Throwable>> handledExceptions(ApplicationConfig appConfig) {
        List<ErrorCode> codes = appConfig.retryPolicy().codes();
        List<Class<? extends Throwable>> exceptions = new ArrayList<>(codes.size());
        for (ErrorCode code : codes) {
            if (code.equals(ErrorCode.BAD_REQUEST)) {
                exceptions.add(ApiBadRequestException.class);
            }
            if (code.equals(ErrorCode.NOT_FOUND)) {
                exceptions.add(ApiNotFoundException.class);
            }
        }
        return exceptions;
    }
}
