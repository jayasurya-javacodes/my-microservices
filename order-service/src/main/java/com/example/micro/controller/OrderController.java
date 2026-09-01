package com.example.micro.controller;


import com.example.micro.client.PaymentClient;
import com.example.micro.client.UserClient;
import com.example.micro.dto.CreateOrderRequest;
import com.example.micro.dto.OrderResponse;
import com.example.micro.dto.UserResponse;
import com.example.micro.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentClient paymentClient;
    private final OrderService orderService;
    private final UserClient userClient;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/get/{id}")
    public CompletableFuture<?> getOrderById(@PathVariable Long id) {

        return orderService
                .getOrderById(id)
                .thenApply(ResponseEntity::ok);
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request)
                .thenApply(response ->
                        ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<UserResponse>> getUserById(@PathVariable Long userId) {

        //UserResponse user = userClient.getUserById(userId);

        return userClient.getUserById(userId)
                .thenApply(ResponseEntity::ok);
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/process-payment")
    public CompletableFuture<String> processPayment() {
        return paymentClient.processPayment();
    }
}
