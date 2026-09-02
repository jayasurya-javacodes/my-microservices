package com.example.micro.config;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {

    public static final String CORRELATION_ID = "X-Correlation-ID";

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution)
            throws IOException {

        String correlationId = MDC.get("CORRELATION_ID");

        if (correlationId != null &&
                !correlationId.isBlank()) {

            //adds CORRELATION_ID to the outgoing request
            request.getHeaders().set(
                    CORRELATION_ID,
                    correlationId
            );
        }

        return execution.execute(request, body);
    }
}
