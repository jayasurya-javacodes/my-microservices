package com.example.micro.service;

import com.example.micro.client.UserClient;
import com.example.micro.dto.OrderResponse;
import com.example.micro.dto.UserResponse;
import com.example.micro.exception.OrderNotFoundException;
import com.example.micro.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private final UserClient userClient;

    private final Map<Long, Order> orders = new HashMap<>();

    public OrderService(UserClient userClient) {
        this.userClient = userClient;

        orders.put(
                101L,
                new Order(
                        101L,
                        1L,
                        "Laptop"
                )
        );
        orders.put(
                102L,
                new Order(
                        102L,
                        2L,
                        "Phone"
                )
        );
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orders.get(orderId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with given Id: "+orderId);
        }

        UserResponse user = userClient.getUserById(order.getUserId());

        return new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getProduct(),
                user.getName(),
                user.getEmail()
        );
    }
}
