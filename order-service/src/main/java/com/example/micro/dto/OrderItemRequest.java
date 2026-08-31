package com.example.micro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "productId required")
    @Positive
    private Long productId;

    @NotNull(message = "select your quantity")
    @Positive
    private Integer quantity;
}
