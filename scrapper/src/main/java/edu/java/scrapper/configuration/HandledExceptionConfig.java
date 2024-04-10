package edu.java.scrapper.configuration;

import edu.java.models.exceptions.ApiInternalServerErrorException;
import edu.java.models.exceptions.ApiServerBadGatewayException;
import edu.java.models.exceptions.ApiServerGatewayTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandledExceptionConfig {

    @SuppressWarnings("MagicNumber")
    @Bean
    public List<Class<? extends Throwable>> handledExceptions(ApplicationConfig appConfig) {
        List<Integer> codes = appConfig.retryPolicy().codes();
        List<Class<? extends Throwable>> exceptions = new ArrayList<>(codes.size());
        for (Integer code : codes) {
            if (code.equals(500)) {
                exceptions.add(ApiInternalServerErrorException.class);
            }
            if (code.equals(502)) {
                exceptions.add(ApiServerBadGatewayException.class);
            }
            if (code.equals(504)) {
                exceptions.add(ApiServerGatewayTimeoutException.class);
            }
        }
        return exceptions;
    }
}
