package com.example.orderservice.dto;

public class OrderStatusResponse {

    private Long orderId;
    private String status;
    private String message;
    private double total;

    public OrderStatusResponse(Long orderId, String status, String message, double total) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.total = total;
    }

    // getters
}
