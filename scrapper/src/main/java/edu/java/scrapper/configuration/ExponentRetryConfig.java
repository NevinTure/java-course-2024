package edu.java.scrapper.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "app", name = "retry-policy.mode", havingValue = "exponent")
public class ExponentRetryConfig {

    public static final int MAX_ATTEMPTS = 3;
    public static final int INIT_INTERVAL_MILLIS = 500;
    public static final double MULTIPLIER = 2;
    private final List<Class<? extends Throwable>> handledExceptions;

    @Autowired
    public ExponentRetryConfig(List<Class<? extends Throwable>> handledExceptions) {
        this.handledExceptions = handledExceptions;
    }

    @Bean
    public RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder
            .stateless()
            .retryPolicy(retryPolicy())
            .backOffOptions(INIT_INTERVAL_MILLIS, MULTIPLIER, Long.MAX_VALUE)
            .build();
    }

    @Bean
    public RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptions =
            handledExceptions.stream().collect(Collectors.toMap(v -> v, v -> true, (v1, v2) -> v1));
        return new SimpleRetryPolicy(MAX_ATTEMPTS, exceptions);
    }
}
