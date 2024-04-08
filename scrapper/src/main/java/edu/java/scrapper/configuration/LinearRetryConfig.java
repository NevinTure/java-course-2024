package edu.java.scrapper.configuration;

import edu.java.scrapper.utils.LinearBackOffPolicy;
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
@ConditionalOnProperty(prefix = "app", name = "retry-policy.mode", havingValue = "linear")
public class LinearRetryConfig {

    public static final int MAX_ATTEMPTS = 3;
    public static final int LINEAR_INTERVAL_MILLIS = 500;
    private final List<Class<? extends Throwable>> handledExceptions;

    @Autowired
    public LinearRetryConfig(List<Class<? extends Throwable>> handledExceptions) {
        this.handledExceptions = handledExceptions;
    }

    @Bean
    public RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder
            .stateless()
            .retryPolicy(retryPolicy())
            .backOffPolicy(new LinearBackOffPolicy((long) LINEAR_INTERVAL_MILLIS))
            .build();
    }

    @Bean
    public RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptions =
            handledExceptions.stream().collect(Collectors.toMap(v -> v, v -> true, (v1, v2) -> v1));
        return new SimpleRetryPolicy(MAX_ATTEMPTS, exceptions);
    }
}
