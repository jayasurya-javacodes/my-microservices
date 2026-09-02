package com.example.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class CorrelationIdFilter implements GlobalFilter {

    private static final String CORRELATION_ID = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        //check whether it already contains X-Correlation-ID from incoming request
        String correlationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(CORRELATION_ID);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        final String finalCorrelationId = correlationId;

        log.info(
                "Incoming request: {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI().getPath()
        );

        System.out.println("X-Correlation-ID:"+"[" + finalCorrelationId + "] ");

        //Create request containing the correlation ID
        //Gateway forwards to Order Service
        ServerWebExchange modifiedExchange =
                exchange.mutate()
                        .request(builder ->
                                builder.header(
                                        CORRELATION_ID,
                                        finalCorrelationId))
                        .build();

        modifiedExchange.getResponse()
                .getHeaders()
                .set(CORRELATION_ID, finalCorrelationId);

        return chain.filter(modifiedExchange);
    }
}