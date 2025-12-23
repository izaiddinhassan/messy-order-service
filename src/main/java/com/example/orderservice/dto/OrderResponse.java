package com.example.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderResponse {
    private Long orderId;
    private double total;
    private String status;

    public OrderResponse(Long orderId, double total, String status) {
        this.orderId = orderId;
        this.total = total;
        this.status = status;
    }

}
