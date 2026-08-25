package com.example.micro.client;

import com.example.micro.exception.PaymentServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://PAYMENT-SERVICE")
                .build();
    }

    @Retry(name = "paymentRetry")
    @TimeLimiter(name = "paymentTimeLimiter")
    @CircuitBreaker(name = "paymentCircuitBreaker", fallbackMethod = "paymentFallback")
    public CompletableFuture<String> processPayment()  {

        return CompletableFuture.supplyAsync(() -> {

            return restClient.get()
                    .uri("/payment/process")
                    .retrieve()
                    .body(String.class);

        });
    }

    public CompletableFuture<String> paymentFallback(Throwable throwable) {

        System.out.println("PAYMENT FALLBACK CALLED: " + throwable.getClass().getSimpleName());

        return CompletableFuture.completedFuture(
                "Payment service is temporarily unavailable. Please try again later."
        );
    }
}
