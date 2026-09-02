package com.example.micro.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID = "X-Correlation-ID";
    public static final String MDC_CORRELATION_ID = "CORRELATION_ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String correlationId = request.getHeader(CORRELATION_ID);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();

        }
        try {

            // Store correlation ID in MDC
            MDC.put(MDC_CORRELATION_ID, correlationId);

            response.setHeader(
                    CORRELATION_ID,
                    correlationId
            );

            log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

//            System.out.println("[" + correlationId + "]"
//                    + request.getMethod()
//                    + "  "
//                    + request.getRequestURI());

            filterChain.doFilter(request, response);

            log.info("Request completed: {} {} status={}", request.getMethod(), request.getRequestURI(), response.getStatus());

        } finally {
            MDC.remove(MDC_CORRELATION_ID);
        }
    }
}
