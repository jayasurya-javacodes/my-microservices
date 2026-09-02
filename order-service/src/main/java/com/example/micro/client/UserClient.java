package com.example.micro.client;

import com.example.micro.dto.UserResponse;
import com.example.micro.exception.UserServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://USER-SERVICE")
                .build();
    }


    @Retry(name = "userRetry")
    @TimeLimiter(name = "userTimeLimiter")
    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "userFallback")
    public CompletableFuture<UserResponse> getUserById(Long userId) {

        // Capture correlation ID BEFORE entering async thread
        String correlationId = MDC.get("CORRELATION_ID");

        return CompletableFuture.supplyAsync(() -> {

                    try {

                        // Put the correlation ID into the async thread's MDC
                        if (correlationId != null) {
                            MDC.put("CORRELATION_ID", correlationId);
                        }

                        return restClient.get()
                                .uri("/users/get/{id}", userId)
                                .retrieve()
                                .body(UserResponse.class);

                    } finally {
                        MDC.remove("CORRELATION_ID");
                    }
                }
        );

    }

    public CompletableFuture<UserResponse> userFallback(
            Long userId,
            Throwable throwable) {

        System.out.println("User Circuit Breaker fallback triggered");
        System.out.println("Reason: " + throwable.getMessage());

        return CompletableFuture.failedFuture(
                new UserServiceUnavailableException(
                        "User Service is currently unavailable, please try again"
                )
        );
    }

}
