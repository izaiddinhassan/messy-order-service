package com.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long productId;
    private Integer quantity;
    private Double priceAtPurchase;
}
