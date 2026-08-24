package com.example.micro.controller;


import com.example.micro.client.PaymentClient;
import com.example.micro.client.UserClient;
import com.example.micro.dto.OrderResponse;
import com.example.micro.dto.UserResponse;
import com.example.micro.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentClient paymentClient;
    private final OrderService orderService;
    private final UserClient userClient;

    @GetMapping("/get/{id}")
    public CompletableFuture<?> getOrderById(@PathVariable Long id) {

        return orderService
                .getOrderById(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<UserResponse>> getUserById(@PathVariable Long userId) {

        //UserResponse user = userClient.getUserById(userId);

        return userClient.getUserById(userId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/process-payment")
    public CompletableFuture<String> processPayment() {
        return paymentClient.processPayment();
    }
}
