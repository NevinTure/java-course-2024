package edu.java.bot.configuration;

import edu.java.models.exceptions.ApiInternalServerErrorException;
import edu.java.models.exceptions.ApiServerBadGatewayException;
import edu.java.models.exceptions.ApiServerGatewayTimeoutException;
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
            if (code.equals(ErrorCode.INTERNAL_SERVER_ERROR)) {
                exceptions.add(ApiInternalServerErrorException.class);
            }
            if (code.equals(ErrorCode.BAD_GATEWAY)) {
                exceptions.add(ApiServerBadGatewayException.class);
            }
            if (code.equals(ErrorCode.GATEWAY_TIMEOUT)) {
                exceptions.add(ApiServerGatewayTimeoutException.class);
            }
        }
        return exceptions;
    }
}
